package fr.alessdevita.valtherion.server.integrated;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.world.WorldSettings;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/** Lifecycle holder for a single-player server instance. */
public class IntegratedServer {
    private final Valtherion client;
    private final String folderName;
    private final String worldName;
    private final WorldSettings settings;
    private final AtomicBoolean running = new AtomicBoolean();
    private final NetworkSystem networkSystem = new NetworkSystem();
    private Thread serverThread;

    public IntegratedServer() { this(null, "world", "World", null); }
    public IntegratedServer(Valtherion client, String folderName, String worldName, WorldSettings settings) {
        this.client = client;
        this.folderName = folderName;
        this.worldName = worldName;
        this.settings = settings;
    }
    public synchronized void startServerThread() {
        if (serverThread != null) return;
        running.set(true);
        serverThread = new Thread(() -> {
            while (running.get()) {
                try { Thread.sleep(50L); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            }
        }, "Valtherion integrated server");
        serverThread.setDaemon(true);
        serverThread.start();
    }
    public boolean serverIsInRunLoop() { return running.get() && serverThread != null && serverThread.isAlive(); }
    public NetworkSystem getNetworkSystem() { return networkSystem; }
    public void initiateShutdown() {
        running.set(false);
        if (serverThread != null) serverThread.interrupt();
    }
    public String getFolderName() { return folderName; }
    public String getWorldName() { return worldName; }
    public WorldSettings getSettings() { return settings; }

    public static class NetworkSystem {
        public SocketAddress addLocalEndpoint() { return new InetSocketAddress("127.0.0.1", 0); }
    }
}
