package fr.alessdevita.valtherion.network.core;

import fr.alessdevita.valtherion.network.exceptions.NetworkException;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

/**
 * Classe pour gérer une connexion réseau dans Valtherion.
 * Représente une connexion entre un client et un serveur.
 * Utilise un pool de threads pour gérer les paquets de manière asynchrone.
 */
public class Connection {

    // Socket de la connexion
    private Socket socket;

    // Flux de sortie pour envoyer des données
    private DataOutputStream outputStream;

    // Flux d'entrée pour recevoir des données
    private DataInputStream inputStream;

    // Adresse IP de la connexion
    private String ipAddress;

    // Port de la connexion
    private int port;

    // ID unique de la connexion
    private String connectionId;

    // Indique si la connexion est active
    private volatile boolean isConnected;

    // Temps de la dernière activité (en millisecondes)
    private long lastActivityTime;

    // Pool de threads pour gérer les paquets
    private final ExecutorService executorService;

    // File d'attente pour les paquets à envoyer
    private final BlockingQueue<Packet> packetQueue;

    // Thread pour envoyer les paquets
    private Thread sendThread;

    // Thread pour recevoir les paquets
    private Thread receiveThread;

    // Indique si les threads sont en cours d'exécution
    private volatile boolean isRunning;

    // Liste des observateurs de connexion
    private final List<ConnectionListener> listeners;

    // Timeout pour la connexion (en millisecondes)
    private long timeout;

    /**
     * Constructeur de la connexion.
     * @param socket Socket de la connexion.
     * @throws NetworkException Si une erreur se produit lors de l'initialisation.
     */
    public Connection(Socket socket) throws NetworkException {
        this.socket = socket;
        this.connectionId = UUID.randomUUID().toString();
        this.listeners = new ArrayList<>();
        this.packetQueue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newCachedThreadPool();
        this.timeout = 30000; // Timeout par défaut : 30 secondes

        try {
            this.ipAddress = socket.getInetAddress().getHostAddress();
            this.port = socket.getPort();
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.isConnected = true;
            this.lastActivityTime = System.currentTimeMillis();

            // Démarrer les threads d'envoi et de réception
            startThreads();
        } catch (IOException e) {
            throw new NetworkException("Impossible d'initialiser la connexion : " + e.getMessage(), e);
        }
    }

    /**
     * Démarre les threads d'envoi et de réception.
     */
    private void startThreads() {
        isRunning = true;

        // Thread pour envoyer les paquets
        sendThread = new Thread(() -> {
            while (isRunning && isConnected) {
                try {
                    Packet packet = packetQueue.take();
                    sendPacketInternal(packet);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (NetworkException e) {
                    System.err.println("Erreur lors de l'envoi d'un paquet : " + e.getMessage());
                }
            }
        });
        sendThread.start();

        // Thread pour recevoir les paquets
        receiveThread = new Thread(() -> {
            while (isRunning && isConnected) {
                try {
                    Packet packet = receivePacketInternal();
                    if (packet != null) {
                        executorService.submit(() -> notifyPacketReceived(packet));
                    }
                } catch (NetworkException e) {
                    System.err.println("Erreur lors de la réception d'un paquet : " + e.getMessage());
                    close();
                }
            }
        });
        receiveThread.start();
    }

    /**
     * Envoie un paquet de manière asynchrone.
     * @param packet Paquet à envoyer.
     * @throws NetworkException Si une erreur se produit lors de l'ajout à la file.
     */
    public void sendPacket(Packet packet) throws NetworkException {
        if (!isConnected || packet == null) {
            throw new NetworkException("La connexion n'est pas active ou le paquet est null.");
        }

        try {
            packetQueue.put(packet);
            lastActivityTime = System.currentTimeMillis();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NetworkException("Interruption lors de l'ajout du paquet à la file.", e);
        }
    }

    /**
     * Envoie un paquet de manière synchrone (utilisé en interne).
     * @param packet Paquet à envoyer.
     * @throws NetworkException Si une erreur se produit lors de l'envoi.
     */
    private void sendPacketInternal(Packet packet) throws NetworkException {
        try {
            byte[] packetData = packet.serialize();
            outputStream.writeInt(packetData.length); // Écrire la taille du paquet
            outputStream.write(packetData); // Écrire les données du paquet
            outputStream.flush();
            lastActivityTime = System.currentTimeMillis();
        } catch (IOException e) {
            throw new NetworkException("Impossible d'envoyer le paquet : " + e.getMessage(), e);
        }
    }

    /**
     * Reçoit un paquet de manière synchrone (utilisé en interne).
     * @return Paquet reçu ou null si la connexion est fermée.
     * @throws NetworkException Si une erreur se produit lors de la réception.
     */
    private Packet receivePacketInternal() throws NetworkException {
        if (!isConnected) {
            return null;
        }

        try {
            int packetSize = inputStream.readInt(); // Lire la taille du paquet
            if (packetSize <= 0) {
                return null;
            }

            byte[] packetData = new byte[packetSize];
            inputStream.readFully(packetData); // Lire les données du paquet

            // Lire l'ID du paquet pour déterminer son type
            ByteArrayInputStream byteStream = new ByteArrayInputStream(packetData);
            DataInputStream dataStream = new DataInputStream(byteStream);
            int packetId = dataStream.readInt();

            // Créer le paquet en fonction de son ID
            Packet packet = PacketRegistry.createPacket(packetId);
            if (packet != null) {
                packet.deserialize(packetData);
                lastActivityTime = System.currentTimeMillis();
                return packet;
            }
        } catch (IOException e) {
            throw new NetworkException("Impossible de recevoir le paquet : " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * Ferme la connexion.
     */
    public void close() {
        if (!isConnected) {
            return;
        }

        isRunning = false;
        isConnected = false;

        // Arrêter les threads
        if (sendThread != null) {
            sendThread.interrupt();
        }
        if (receiveThread != null) {
            receiveThread.interrupt();
        }

        // Arrêter le pool de threads
        executorService.shutdown();

        // Fermer les flux et le socket
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            notifyDisconnected();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    /**
     * Vérifie si la connexion est active.
     * @return true si la connexion est active, false sinon.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Vérifie si la connexion est toujours active (timeout).
     * @return true si la connexion est active, false sinon.
     */
    public boolean isAlive() {
        return isConnected && (System.currentTimeMillis() - lastActivityTime) < timeout;
    }

    /**
     * Ajoute un observateur de connexion.
     * @param listener Observateur à ajouter.
     */
    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur de connexion.
     * @param listener Observateur à retirer.
     */
    public void removeListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs qu'un paquet a été reçu.
     * @param packet Paquet reçu.
     */
    private void notifyPacketReceived(Packet packet) {
        for (ConnectionListener listener : listeners) {
            listener.onPacketReceived(this, packet);
        }
    }

    /**
     * Notifie les observateurs que la connexion a été fermée.
     */
    private void notifyDisconnected() {
        for (ConnectionListener listener : listeners) {
            listener.onDisconnected(this);
        }
    }

    // --- Getters et Setters ---

    public Socket getSocket() {
        return socket;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Retourne une représentation textuelle de la connexion.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Connection{" +
                "ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", connectionId='" + connectionId + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }

    // --- Interface pour les observateurs de connexion ---
    public interface ConnectionListener {
        void onPacketReceived(Connection connection, Packet packet);
        void onDisconnected(Connection connection);
    }
}