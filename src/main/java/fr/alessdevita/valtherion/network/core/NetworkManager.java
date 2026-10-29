package fr.alessdevita.valtherion.network.core;

import fr.alessdevita.valtherion.network.exceptions.NetworkException;
import fr.alessdevita.valtherion.network.client.ClientNetworkHandler;
import fr.alessdevita.valtherion.network.server.ServerNetworkHandler;

import java.util.*;

/**
 * Gestionnaire réseau principal pour Valtherion.
 * Centralise la gestion des connexions, des paquets, et des observateurs.
 * Implémenté comme un singleton pour une gestion globale.
 */
public class NetworkManager {

    // Instance unique du gestionnaire réseau
    private static NetworkManager instance;

    // Mode du gestionnaire (CLIENT, SERVER, BOTH)
    private NetworkMode mode;

    // Gestionnaire réseau côté client
    private ClientNetworkHandler clientHandler;

    // Gestionnaire réseau côté serveur
    private ServerNetworkHandler serverHandler;

    // Liste des observateurs du gestionnaire
    private final List<NetworkManagerListener> listeners;

    // Indique si le gestionnaire est initialisé
    private boolean isInitialized;
    private Object netHandler;

    public void update() {
    }

    public static NetworkManager provideLocalClient(java.net.SocketAddress address) { return new NetworkManager(); }
    public void setNetHandler(Object handler) { this.netHandler = handler; }
    public Object getNetHandler() { return netHandler; }
    public void scheduleOutboundPacket(Object packet, Object... listeners) { }
    public void processReceivedPackets() { update(); }

    // Énumération des modes du gestionnaire
    public enum NetworkMode {
        CLIENT,  // Mode client uniquement
        SERVER,  // Mode serveur uniquement
        BOTH     // Mode client et serveur
    }

    /**
     * Constructeur privé pour le singleton.
     */
    public NetworkManager() {
        this.mode = NetworkMode.CLIENT;
        this.isInitialized = false;
        this.listeners = new ArrayList<>();
    }

    /**
     * Récupère l'instance unique du gestionnaire réseau.
     * @return Instance du gestionnaire réseau.
     */
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    /**
     * Initialise le gestionnaire en mode client.
     * @param serverIp Adresse IP du serveur.
     * @param serverPort Port du serveur.
     * @param clientName Nom du client.
     * @throws NetworkException Si une erreur se produit lors de l'initialisation.
     */
    public void initClient(String serverIp, int serverPort, String clientName) throws NetworkException {
        this.mode = NetworkMode.CLIENT;
        this.clientHandler = new ClientNetworkHandler(serverIp, serverPort, clientName);
        this.clientHandler.addListener(new ClientNetworkHandler.ClientListener() {
            @Override
            public void onConnected(ClientNetworkHandler handler, Connection connection) {
                notifyClientConnected(connection);
            }

            @Override
            public void onDisconnected(ClientNetworkHandler handler, Connection connection) {
                notifyClientDisconnected(connection);
            }

            @Override
            public void onPacketReceived(ClientNetworkHandler handler, Connection connection, Packet packet) {

            }

            @Override
            public void onConnected(Connection connection) {
                notifyClientConnected(connection);
            }

            @Override
            public void onDisconnected(Connection connection) {
                notifyClientDisconnected(connection);
            }

            @Override
            public void onPacketReceived(Connection connection, Packet packet) {
                notifyPacketReceived(connection, packet);
            }
        });
        this.isInitialized = true;
    }

    /**
     * Initialise le gestionnaire en mode serveur.
     * @param port Port du serveur.
     * @throws NetworkException Si une erreur se produit lors de l'initialisation.
     */
    public void initServer(int port) throws NetworkException {
        this.mode = NetworkMode.SERVER;
        this.serverHandler = new ServerNetworkHandler(port);
        this.serverHandler.addListener(new ServerNetworkHandler.ServerListener() {
            @Override
            public void onStarted(ServerNetworkHandler handler) {
                notifyServerStarted();
            }

            @Override
            public void onStopped(ServerNetworkHandler handler) {
                notifyServerStopped();
            }

            @Override
            public void onClientConnected(ServerNetworkHandler handler, Connection connection) {

            }

            @Override
            public void onClientDisconnected(ServerNetworkHandler handler, Connection connection) {

            }

            @Override
            public void onPacketReceived(ServerNetworkHandler handler, Connection connection, Packet packet) {

            }

            @Override
            public void onStarted() {
                notifyServerStarted();
            }

            @Override
            public void onStopped() {
                notifyServerStopped();
            }

            @Override
            public void onClientConnected(Connection connection) {
                notifyClientConnected(connection);
            }

            @Override
            public void onClientDisconnected(Connection connection) {
                notifyClientDisconnected(connection);
            }

            @Override
            public void onPacketReceived(Connection connection, Packet packet) {
                notifyPacketReceived(connection, packet);
            }
        });
        this.isInitialized = true;
    }

    /**
     * Initialise le gestionnaire en mode client et serveur.
     * @param serverIp Adresse IP du serveur distant.
     * @param serverPort Port du serveur distant.
     * @param clientName Nom du client.
     * @param localServerPort Port du serveur local.
     * @throws NetworkException Si une erreur se produit lors de l'initialisation.
     */
    public void initBoth(String serverIp, int serverPort, String clientName, int localServerPort) throws NetworkException {
        this.mode = NetworkMode.BOTH;

        // Initialiser le client
        initClient(serverIp, serverPort, clientName);

        // Initialiser le serveur local
        this.serverHandler = new ServerNetworkHandler(localServerPort);
        this.serverHandler.addListener(new ServerNetworkHandler.ServerListener() {
            @Override
            public void onStarted(ServerNetworkHandler handler) {
                notifyServerStarted();
            }

            @Override
            public void onStopped(ServerNetworkHandler handler) {
                notifyServerStopped();
            }

            @Override
            public void onClientConnected(ServerNetworkHandler handler, Connection connection) {

            }

            @Override
            public void onClientDisconnected(ServerNetworkHandler handler, Connection connection) {

            }

            @Override
            public void onPacketReceived(ServerNetworkHandler handler, Connection connection, Packet packet) {

            }

            @Override
            public void onStarted() {
                notifyServerStarted();
            }

            @Override
            public void onStopped() {
                notifyServerStopped();
            }

            @Override
            public void onClientConnected(Connection connection) {
                notifyClientConnected(connection);
            }

            @Override
            public void onClientDisconnected(Connection connection) {
                notifyClientDisconnected(connection);
            }

            @Override
            public void onPacketReceived(Connection connection, Packet packet) {
                notifyPacketReceived(connection, packet);
            }
        });
        this.isInitialized = true;
    }

    /**
     * Démarre le gestionnaire.
     * @throws NetworkException Si une erreur se produit lors du démarrage.
     */
    public void start() throws NetworkException {
        if (!isInitialized) {
            throw new NetworkException("Le gestionnaire réseau n'est pas initialisé.");
        }

        switch (mode) {
            case CLIENT:
                if (clientHandler != null) {
                    clientHandler.connect();
                }
                break;
            case SERVER:
                if (serverHandler != null) {
                    serverHandler.start();
                }
                break;
            case BOTH:
                if (clientHandler != null) {
                    clientHandler.connect();
                }
                if (serverHandler != null) {
                    serverHandler.start();
                }
                break;
        }
    }

    /**
     * Arrête le gestionnaire.
     */
    public void stop() {
        if (!isInitialized) {
            return;
        }

        switch (mode) {
            case CLIENT:
                if (clientHandler != null) {
                    clientHandler.disconnect();
                }
                break;
            case SERVER:
                if (serverHandler != null) {
                    serverHandler.stop();
                }
                break;
            case BOTH:
                if (clientHandler != null) {
                    clientHandler.disconnect();
                }
                if (serverHandler != null) {
                    serverHandler.stop();
                }
                break;
        }
    }

    /**
     * Envoie un paquet.
     * @param packet Paquet à envoyer.
     * @throws NetworkException Si une erreur se produit lors de l'envoi.
     */
    public void sendPacket(Packet packet) throws NetworkException {
        if (!isInitialized) {
            throw new NetworkException("Le gestionnaire réseau n'est pas initialisé.");
        }

        switch (mode) {
            case CLIENT:
                if (clientHandler != null) {
                    clientHandler.sendPacket(packet);
                }
                break;
            case SERVER:
                if (serverHandler != null) {
                    serverHandler.broadcastPacket(packet);
                }
                break;
            case BOTH:
                if (clientHandler != null) {
                    clientHandler.sendPacket(packet);
                }
                break;
        }
    }

    /**
     * Envoie un paquet à une connexion spécifique.
     * @param packet Paquet à envoyer.
     * @param connection Connexion cible.
     * @throws NetworkException Si une erreur se produit lors de l'envoi.
     */
    public void sendPacketTo(Packet packet, Connection connection) throws NetworkException {
        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

    /**
     * Ajoute un observateur du gestionnaire.
     * @param listener Observateur à ajouter.
     */
    public void addListener(NetworkManagerListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur du gestionnaire.
     * @param listener Observateur à retirer.
     */
    public void removeListener(NetworkManagerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs que le serveur a démarré.
     */
    private void notifyServerStarted() {
        for (NetworkManagerListener listener : listeners) {
            listener.onServerStarted();
        }
    }

    /**
     * Notifie les observateurs que le serveur s'est arrêté.
     */
    private void notifyServerStopped() {
        for (NetworkManagerListener listener : listeners) {
            listener.onServerStopped();
        }
    }

    /**
     * Notifie les observateurs qu'un client s'est connecté.
     * @param connection Connexion du client.
     */
    private void notifyClientConnected(Connection connection) {
        for (NetworkManagerListener listener : listeners) {
            listener.onClientConnected(connection);
        }
    }

    /**
     * Notifie les observateurs qu'un client s'est déconnecté.
     * @param connection Connexion du client.
     */
    private void notifyClientDisconnected(Connection connection) {
        for (NetworkManagerListener listener : listeners) {
            listener.onClientDisconnected(connection);
        }
    }

    /**
     * Notifie les observateurs qu'un paquet a été reçu.
     * @param connection Connexion associée.
     * @param packet Paquet reçu.
     */
    private void notifyPacketReceived(Connection connection, Packet packet) {
        for (NetworkManagerListener listener : listeners) {
            listener.onPacketReceived(connection, packet);
        }
    }

    // --- Getters ---

    public NetworkMode getMode() {
        return mode;
    }

    public ClientNetworkHandler getClientHandler() {
        return clientHandler;
    }

    public ServerNetworkHandler getServerHandler() {
        return serverHandler;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Retourne une représentation textuelle du gestionnaire réseau.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "NetworkManager{" +
                "mode=" + mode +
                ", isInitialized=" + isInitialized +
                '}';
    }

    // --- Interface pour les observateurs du gestionnaire réseau ---
    public interface NetworkManagerListener {
        void onServerStarted();
        void onServerStopped();
        void onClientConnected(Connection connection);
        void onClientDisconnected(Connection connection);
        void onPacketReceived(Connection connection, Packet packet);
    }
}
