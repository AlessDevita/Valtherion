package fr.alessdevita.valtherion.network.client;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.network.core.PacketHandler;
import fr.alessdevita.valtherion.network.exceptions.NetworkException;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Gestionnaire réseau côté client pour Valtherion.
 * Gère la connexion au serveur, l'envoi et la réception des paquets.
 */
public class ClientNetworkHandler {

    // Connexion au serveur
    private Connection connection;

    // Adresse IP du serveur
    private String serverIp;

    // Port du serveur
    private int serverPort;

    // Nom du client
    private String clientName;

    // Indique si le client est connecté
    private boolean isConnected;

    // Liste des observateurs du gestionnaire
    private final List<ClientListener> listeners;

    // File d'attente pour les paquets à envoyer
    private final List<Packet> pendingPackets;

    /**
     * Constructeur du gestionnaire réseau côté client.
     * @param serverIp Adresse IP du serveur.
     * @param serverPort Port du serveur.
     * @param clientName Nom du client.
     */
    public ClientNetworkHandler(String serverIp, int serverPort, String clientName) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientName = clientName;
        this.isConnected = false;
        this.listeners = new ArrayList<>();
        this.pendingPackets = new ArrayList<>();
    }

    /**
     * Connecte le client au serveur.
     * @throws NetworkException Si une erreur se produit lors de la connexion.
     */
    public void connect() throws NetworkException {
        if (isConnected) {
            throw new NetworkException("Le client est déjà connecté.");
        }

        try {
            Socket socket = new Socket(serverIp, serverPort);
            connection = new Connection(socket);
            connection.addListener(new Connection.ConnectionListener() {
                @Override
                public void onPacketReceived(Connection connection, Packet packet) {
                    PacketHandler.handlePacketClient(packet, connection);
                    notifyPacketReceived(connection, packet);
                }

                @Override
                public void onDisconnected(Connection connection) {
                    isConnected = false;
                    notifyDisconnected(connection);
                }
            });

            isConnected = true;
            notifyConnected(connection);

            // Envoyer les paquets en attente
            sendPendingPackets();
        } catch (IOException e) {
            throw new NetworkException("Impossible de se connecter au serveur : " + e.getMessage(), e);
        }
    }

    /**
     * Déconnecte le client du serveur.
     */
    public void disconnect() {
        if (!isConnected) {
            return;
        }

        if (connection != null) {
            connection.close();
        }
        isConnected = false;
        notifyDisconnected(connection);
    }

    /**
     * Envoie un paquet au serveur.
     * @param packet Paquet à envoyer.
     * @throws NetworkException Si une erreur se produit lors de l'envoi.
     */
    public void sendPacket(Packet packet) throws NetworkException {
        if (!isConnected) {
            // Si le client n'est pas connecté, ajouter le paquet à la file d'attente
            pendingPackets.add(packet);
            return;
        }

        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

    /**
     * Envoie les paquets en attente.
     */
    private void sendPendingPackets() {
        if (!isConnected || connection == null) {
            return;
        }

        for (Packet packet : pendingPackets) {
            try {
                connection.sendPacket(packet);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de l'envoi d'un paquet en attente : " + e.getMessage());
            }
        }
        pendingPackets.clear();
    }

    /**
     * Ajoute un observateur du gestionnaire.
     * @param listener Observateur à ajouter.
     */
    public void addListener(ClientListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur du gestionnaire.
     * @param listener Observateur à retirer.
     */
    public void removeListener(ClientListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs que le client est connecté.
     * @param connection Connexion associée.
     */
    private void notifyConnected(Connection connection) {
        for (ClientListener listener : listeners) {
            listener.onConnected(this, connection);
        }
    }

    /**
     * Notifie les observateurs que le client est déconnecté.
     * @param connection Connexion associée.
     */
    private void notifyDisconnected(Connection connection) {
        for (ClientListener listener : listeners) {
            listener.onDisconnected(this, connection);
        }
    }

    /**
     * Notifie les observateurs qu'un paquet a été reçu.
     * @param connection Connexion associée.
     * @param packet Paquet reçu.
     */
    private void notifyPacketReceived(Connection connection, Packet packet) {
        for (ClientListener listener : listeners) {
            listener.onPacketReceived(this, connection, packet);
        }
    }

    // --- Getters ---

    public Connection getConnection() {
        return connection;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getClientName() {
        return clientName;
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Retourne une représentation textuelle du gestionnaire réseau côté client.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ClientNetworkHandler{" +
                "serverIp='" + serverIp + '\'' +
                ", serverPort=" + serverPort +
                ", clientName='" + clientName + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }

    // --- Interface pour les observateurs du gestionnaire côté client ---
    public interface ClientListener {
        void onConnected(ClientNetworkHandler handler, Connection connection);
        void onDisconnected(ClientNetworkHandler handler, Connection connection);
        void onPacketReceived(ClientNetworkHandler handler, Connection connection, Packet packet);

        void onConnected(Connection connection);

        void onDisconnected(Connection connection);

        void onPacketReceived(Connection connection, Packet packet);
    }
}