package fr.alessdevita.valtherion.client.multiplayer;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/** Écoute les annonces UDP de serveurs LAN au format MOTD*message*port. */
public class TreadLanPing implements Closeable, Runnable {
    public static final int DEFAULT_PORT = 4445;
    private static final String DISCOVERY_PACKET = "WHOIS";
    private static final int MAX_PACKET_SIZE = 2048;

    private final int discoveryPort;
    private final Consumer<ServerData> listener;
    private final AtomicBoolean running = new AtomicBoolean();
    private volatile DatagramSocket socket;
    private volatile Thread thread;

    public TreadLanPing(Consumer<ServerData> listener) { this(DEFAULT_PORT, listener); }

    public TreadLanPing(int discoveryPort, Consumer<ServerData> listener) {
        if (discoveryPort < 1 || discoveryPort > 65535) throw new IllegalArgumentException("Port LAN invalide.");
        this.discoveryPort = discoveryPort;
        this.listener = Objects.requireNonNull(listener, "listener");
    }

    public synchronized void start() {
        if (!this.running.compareAndSet(false, true)) return;
        this.thread = new Thread(this, "valtherion-lan-discovery");
        this.thread.setDaemon(true);
        this.thread.start();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[MAX_PACKET_SIZE];
        try (DatagramSocket datagramSocket = new DatagramSocket(null)) {
            this.socket = datagramSocket;
            datagramSocket.setReuseAddress(true);
            datagramSocket.bind(new InetSocketAddress(this.discoveryPort));
            datagramSocket.setSoTimeout(1000);
            sendDiscoveryRequest(datagramSocket);

            while (this.running.get()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    datagramSocket.receive(packet);
                    ServerData server = parseAnnouncement(new String(packet.getData(), packet.getOffset(),
                            packet.getLength(), StandardCharsets.UTF_8), packet.getAddress());
                    if (server != null) this.listener.accept(server);
                } catch (java.net.SocketTimeoutException ignored) {
                    // Le timeout permet de vérifier régulièrement la demande d'arrêt.
                }
            }
        } catch (SocketException exception) {
            if (this.running.get()) System.err.println("Détection LAN impossible : " + exception.getMessage());
        } catch (IOException exception) {
            if (this.running.get()) System.err.println("Erreur pendant la détection LAN : " + exception.getMessage());
        } finally {
            this.socket = null;
            this.running.set(false);
        }
    }

    private void sendDiscoveryRequest(DatagramSocket datagramSocket) {
        try {
            byte[] payload = DISCOVERY_PACKET.getBytes(StandardCharsets.UTF_8);
            DatagramPacket request = new DatagramPacket(payload, payload.length,
                    InetAddress.getByName("255.255.255.255"), this.discoveryPort);
            datagramSocket.setBroadcast(true);
            datagramSocket.send(request);
        } catch (IOException exception) {
            System.err.println("Impossible de rechercher les serveurs LAN : " + exception.getMessage());
        }
    }

    public static ServerData parseAnnouncement(String message, InetAddress sourceAddress) {
        if (message == null || sourceAddress == null) return null;
        String[] fields = message.trim().split("\\*", 3);
        if (fields.length != 3 || !"MOTD".equals(fields[0])) return null;
        try {
            int gamePort = Integer.parseInt(fields[2]);
            if (gamePort < 1 || gamePort > 65535) return null;
            String name = fields[1].trim();
            if (name.isEmpty()) name = "Serveur LAN";
            ServerData server = new ServerData(name,
                    sourceAddress.getHostAddress() + ":" + gamePort, true);
            server.setMOTD(fields[1]);
            return server;
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public boolean isRunning() { return this.running.get(); }

    @Override
    public void close() {
        this.running.set(false);
        DatagramSocket currentSocket = this.socket;
        if (currentSocket != null) currentSocket.close();
        Thread currentThread = this.thread;
        if (currentThread != null) currentThread.interrupt();
    }
}
