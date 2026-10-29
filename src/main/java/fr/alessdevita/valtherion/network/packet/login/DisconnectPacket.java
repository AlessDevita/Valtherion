package fr.alessdevita.valtherion.network.packet.login;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;

/**
 * Paquet de déconnexion dans Valtherion.
 * Utilisé pour notifier une déconnexion (volontaire ou forcée) entre le client et le serveur.
 */
public class DisconnectPacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 1003;

    // Nom du joueur
    private String playerName;

    // ID du joueur
    private String playerId;

    // Raison de la déconnexion
    private String reason;

    // Indique si la déconnexion est volontaire
    private boolean isVoluntary;

    // Code de déconnexion (pour les déconnexions forcées)
    private DisconnectCode disconnectCode;

    // Énumération des codes de déconnexion
    public enum DisconnectCode {
        NONE,           // Aucune raison spécifique
        TIMEOUT,        // Timeout de la connexion
        KICK,           // Kick par un administrateur
        BAN,            // Ban par un administrateur
        VERSION_MISMATCH, // Version du client non supportée
        INVALID_CREDENTIALS, // Identifiants invalides
        SERVER_SHUTDOWN, // Arrêt du serveur
        ERROR           // Erreur interne
    }

    /**
     * Constructeur du paquet de déconnexion.
     */
    public DisconnectPacket() {
        super(PACKET_ID);
        this.disconnectCode = DisconnectCode.NONE;
        this.isVoluntary = true;
    }

    /**
     * Constructeur du paquet de déconnexion avec un nom de joueur et une raison.
     * @param playerName Nom du joueur.
     * @param reason Raison de la déconnexion.
     */
    public DisconnectPacket(String playerName, String reason) {
        this();
        this.playerName = playerName;
        this.reason = reason;
    }

    /**
     * Constructeur du paquet de déconnexion avec un nom de joueur, un ID et une raison.
     * @param playerName Nom du joueur.
     * @param playerId ID du joueur.
     * @param reason Raison de la déconnexion.
     */
    public DisconnectPacket(String playerName, String playerId, String reason) {
        this(playerName, reason);
        this.playerId = playerId;
    }

    /**
     * Constructeur du paquet de déconnexion avec un nom de joueur, un ID, une raison et un code.
     * @param playerName Nom du joueur.
     * @param playerId ID du joueur.
     * @param reason Raison de la déconnexion.
     * @param disconnectCode Code de déconnexion.
     */
    public DisconnectPacket(String playerName, String playerId, String reason, DisconnectCode disconnectCode) {
        this(playerName, playerId, reason);
        this.disconnectCode = disconnectCode;
        this.isVoluntary = false;
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire le nom du joueur
        dataStream.writeUTF(playerName != null ? playerName : "");

        // Écrire l'ID du joueur (null si non fourni)
        dataStream.writeBoolean(playerId != null);
        if (playerId != null) {
            dataStream.writeUTF(playerId);
        }

        // Écrire la raison
        dataStream.writeUTF(reason != null ? reason : "");

        // Écrire le flag de déconnexion volontaire
        dataStream.writeBoolean(isVoluntary);

        // Écrire le code de déconnexion
        dataStream.writeByte(disconnectCode.ordinal());
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire le nom du joueur
        this.playerName = dataStream.readUTF();

        // Lire l'ID du joueur
        if (dataStream.readBoolean()) {
            this.playerId = dataStream.readUTF();
        }

        // Lire la raison
        this.reason = dataStream.readUTF();

        // Lire le flag de déconnexion volontaire
        this.isVoluntary = dataStream.readBoolean();

        // Lire le code de déconnexion
        this.disconnectCode = DisconnectCode.values()[dataStream.readByte()];
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite la déconnexion
        if (isVoluntary) {
            System.out.println("Déconnexion volontaire : " + reason);
        } else {
            System.err.println("Déconnexion forcée : " + reason + " (code : " + disconnectCode + ")");
        }

        // Ici, tu peux nettoyer les ressources côté client
        // Exemple : Fermer la connexion, afficher un message à l'utilisateur, etc.
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite la déconnexion
        System.out.println("Déconnexion de " + playerName + " : " + reason);

        // Ici, tu peux nettoyer les ressources côté serveur
        // Exemple : Retirer le joueur de la liste des joueurs connectés
    }

    // --- Getters et Setters ---

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isVoluntary() {
        return isVoluntary;
    }

    public void setVoluntary(boolean isVoluntary) {
        this.isVoluntary = isVoluntary;
    }

    public DisconnectCode getDisconnectCode() {
        return disconnectCode;
    }

    public void setDisconnectCode(DisconnectCode disconnectCode) {
        this.disconnectCode = disconnectCode;
    }

    /**
     * Retourne une représentation textuelle du paquet de déconnexion.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "DisconnectPacket{" +
                "playerName='" + playerName + '\'' +
                ", playerId='" + playerId + '\'' +
                ", reason='" + reason + '\'' +
                ", isVoluntary=" + isVoluntary +
                ", disconnectCode=" + disconnectCode +
                '}';
    }
}