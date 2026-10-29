package fr.alessdevita.valtherion.network.client;

import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.exceptions.NetworkException;

import java.net.Socket;
import java.util.*;

/**
 * Connexion côté serveur dans Valtherion.
 * Représente une connexion entre le serveur et un client, avec des informations supplémentaires sur le joueur.
 */
public class ServerConnection extends Connection {

    // ID du joueur associé à cette connexion
    private String playerId;

    // Nom du joueur associé à cette connexion
    private String playerName;

    // Dernier paquet reçu
    private Packet lastPacket;

    // Compteur de paquets reçus
    private int packetCount;

    // Timestamp de la dernière activité du joueur (en millisecondes)
    private long lastPlayerActivity;

    // Indique si le joueur est authentifié
    private boolean isAuthenticated;

    // Rôle du joueur (ADMIN, MODERATOR, PLAYER)
    private PlayerRole playerRole;

    // Énumération des rôles de joueur
    public enum PlayerRole {
        PLAYER,      // Joueur normal
        MODERATOR,   // Modérateur
        ADMIN        // Administrateur
    }

    /**
     * Constructeur de la connexion côté serveur.
     * @param socket Socket de la connexion.
     * @throws NetworkException Si une erreur se produit lors de l'initialisation.
     */
    public ServerConnection(Socket socket) throws NetworkException {
        super(socket);
        this.packetCount = 0;
        this.lastPlayerActivity = System.currentTimeMillis();
        this.isAuthenticated = false;
        this.playerRole = PlayerRole.PLAYER;
    }

    /**
     * Met à jour la dernière activité du joueur.
     */
    public void updateLastPlayerActivity() {
        this.lastPlayerActivity = System.currentTimeMillis();
    }

    /**
     * Incrémente le compteur de paquets.
     */
    public void incrementPacketCount() {
        this.packetCount++;
    }

    /**
     * Définit le dernier paquet reçu.
     * @param packet Dernier paquet reçu.
     */
    public void setLastPacket(Packet packet) {
        this.lastPacket = packet;
        incrementPacketCount();
        updateLastPlayerActivity();
    }

    /**
     * Authentifie le joueur.
     * @param playerId ID du joueur.
     * @param playerName Nom du joueur.
     * @param playerRole Rôle du joueur.
     */
    public void authenticate(String playerId, String playerName, PlayerRole playerRole) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerRole = playerRole;
        this.isAuthenticated = true;
        updateLastPlayerActivity();
    }

    /**
     * Déconnecte le joueur.
     */
    public void disconnectPlayer() {
        this.isAuthenticated = false;
        this.playerId = null;
        this.playerName = null;
        this.playerRole = PlayerRole.PLAYER;
    }

    /**
     * Vérifie si le joueur est inactif.
     * @param timeout Timeout en millisecondes.
     * @return true si le joueur est inactif, false sinon.
     */
    public boolean isPlayerInactive(long timeout) {
        return !isAuthenticated || (System.currentTimeMillis() - lastPlayerActivity) > timeout;
    }

    /**
     * Vérifie si le joueur a un rôle spécifique.
     * @param role Rôle à vérifier.
     * @return true si le joueur a le rôle, false sinon.
     */
    public boolean hasRole(PlayerRole role) {
        return isAuthenticated && playerRole.ordinal() >= role.ordinal();
    }

    // --- Getters et Setters ---

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Packet getLastPacket() {
        return lastPacket;
    }

    public int getPacketCount() {
        return packetCount;
    }

    public long getLastPlayerActivity() {
        return lastPlayerActivity;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    /**
     * Retourne une représentation textuelle de la connexion côté serveur.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ServerConnection{" +
                "ipAddress='" + getIpAddress() + '\'' +
                ", port=" + getPort() +
                ", playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", playerRole=" + playerRole +
                ", packetCount=" + packetCount +
                '}';
    }
}