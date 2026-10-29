package fr.alessdevita.valtherion.network.server;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.network.core.PacketHandler;
import fr.alessdevita.valtherion.network.exceptions.NetworkException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

/**
 * Gestionnaire réseau côté serveur pour Valtherion.
 * Gère les connexions des clients, l'envoi et la réception des paquets.
 */
public class ServerNetworkHandler {

    // ServerSocket du serveur
    private ServerSocket serverSocket;

    // Port du serveur
    private int port;

    // Indique si le serveur est en cours d'exécution
    private boolean isRunning;

    // Thread pour accepter les connexions
    private Thread acceptThread;

    // Liste des connexions actives
    private final List<Connection> connections;

    // Pool de threads pour gérer les connexions
    private final ExecutorService executorService;

    // Liste des observateurs du gestionnaire
    private final List<ServerListener> listeners;

    // Timeout pour les connexions (en millisecondes)
    private long connectionTimeout;

    /**
     * Constructeur du gestionnaire réseau côté serveur.
     * @param port Port du serveur.
     */
    public ServerNetworkHandler(int port) {
        this.port = port;
        this.isRunning = false;
        this.connections = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        this.listeners = new CopyOnWriteArrayList<>();
        this.connectionTimeout = 30000; // Timeout par défaut : 30 secondes
    }

    /**
     * Démarre le serveur.
     * @throws NetworkException Si une erreur se produit lors du démarrage.
     */
    public void start() throws NetworkException {
        if (isRunning) {
            throw new NetworkException("Le serveur est déjà en cours d'exécution.");
        }

        try {
            serverSocket = new ServerSocket(port);
            isRunning = true;

            // Démarrer le thread pour accepter les connexions
            acceptThread = new Thread(() -> {
                while (isRunning) {
                    try {
                        Socket socket = serverSocket.accept();
                        socket.setSoTimeout((int) connectionTimeout);
                        Connection connection = new Connection(socket);
                        connections.add(connection);

                        // Ajouter un observateur à la connexion
                        connection.addListener(new Connection.ConnectionListener() {
                            @Override
                            public void onPacketReceived(Connection connection, Packet packet) {
                                PacketHandler.handlePacketServer(packet, connection);
                                notifyPacketReceived(connection, packet);
                            }

                            @Override
                            public void onDisconnected(Connection connection) {
                                connections.remove(connection);
                                notifyClientDisconnected(connection);
                            }
                        });

                        // Démarrer un thread pour gérer la connexion
                        executorService.submit(() -> handleConnection(connection));

                        notifyClientConnected(connection);
                    } catch (IOException e) {
                        if (isRunning) {
                            System.err.println("Erreur lors de l'acceptation d'une connexion : " + e.getMessage());
                        }
                    } catch (NetworkException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            acceptThread.start();

            notifyStarted();
        } catch (IOException e) {
            throw new NetworkException("Impossible de démarrer le serveur : " + e.getMessage(), e);
        }
    }

    /**
     * Arrête le serveur.
     */
    public void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;

        // Fermer toutes les connexions
        for (Connection connection : connections) {
            connection.close();
        }
        connections.clear();

        // Fermer le ServerSocket
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du serveur : " + e.getMessage());
        }

        // Arrêter le thread d'acceptation
        if (acceptThread != null) {
            acceptThread.interrupt();
        }

        // Arrêter le pool de threads
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        notifyStopped();
    }

    /**
     * Gère une connexion client.
     * @param connection Connexion à gérer.
     */
    private void handleConnection(Connection connection) {
        while (isRunning && connection.isConnected() && connection.isAlive()) {
            try {
                // La connexion est gérée par son propre thread via les observateurs
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Envoie un paquet à tous les clients connectés.
     * @param packet Paquet à envoyer.
     * @throws NetworkException Si une erreur se produit lors de l'envoi.
     */
    public void broadcastPacket(Packet packet) throws NetworkException {
        for (Connection connection : connections) {
            try {
                connection.sendPacket(packet);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de l'envoi d'un paquet à " + connection.getIpAddress() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Envoie un paquet à un client spécifique.
     * @param packet Paquet à envoyer.
     * @param connection Connexion du client.
     * @throws NetworkException Si une erreur se produit lors de l'envoi.
     */
    public void sendPacketTo(Packet packet, Connection connection) throws NetworkException {
        if (connection != null && connections.contains(connection)) {
            connection.sendPacket(packet);
        }
    }

    /**
     * Ajoute un observateur du gestionnaire.
     * @param listener Observateur à ajouter.
     */
    public void addListener(ServerListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur du gestionnaire.
     * @param listener Observateur à retirer.
     */
    public void removeListener(ServerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs que le serveur a démarré.
     */
    private void notifyStarted() {
        for (ServerListener listener : listeners) {
            listener.onStarted(this);
        }
    }

    /**
     * Notifie les observateurs que le serveur s'est arrêté.
     */
    private void notifyStopped() {
        for (ServerListener listener : listeners) {
            listener.onStopped(this);
        }
    }

    /**
     * Notifie les observateurs qu'un client s'est connecté.
     * @param connection Connexion du client.
     */
    private void notifyClientConnected(Connection connection) {
        for (ServerListener listener : listeners) {
            listener.onClientConnected(this, connection);
        }
    }

    /**
     * Notifie les observateurs qu'un client s'est déconnecté.
     * @param connection Connexion du client.
     */
    private void notifyClientDisconnected(Connection connection) {
        for (ServerListener listener : listeners) {
            listener.onClientDisconnected(this, connection);
        }
    }

    /**
     * Notifie les observateurs qu'un paquet a été reçu.
     * @param connection Connexion du client.
     * @param packet Paquet reçu.
     */
    private void notifyPacketReceived(Connection connection, Packet packet) {
        for (ServerListener listener : listeners) {
            listener.onPacketReceived(this, connection, packet);
        }
    }

    // --- Getters et Setters ---

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public int getClientCount() {
        return connections.size();
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Retourne une représentation textuelle du gestionnaire réseau côté serveur.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ServerNetworkHandler{" +
                "port=" + port +
                ", isRunning=" + isRunning +
                ", clientCount=" + getClientCount() +
                '}';
    }

    // --- Interface pour les observateurs du gestionnaire côté serveur ---
    public interface ServerListener {
        void onStarted(ServerNetworkHandler handler);
        void onStopped(ServerNetworkHandler handler);
        void onClientConnected(ServerNetworkHandler handler, Connection connection);
        void onClientDisconnected(ServerNetworkHandler handler, Connection connection);
        void onPacketReceived(ServerNetworkHandler handler, Connection connection, Packet packet);

        void onStarted();

        void onStopped();

        void onClientConnected(Connection connection);

        void onClientDisconnected(Connection connection);

        void onPacketReceived(Connection connection, Packet packet);
    }
}