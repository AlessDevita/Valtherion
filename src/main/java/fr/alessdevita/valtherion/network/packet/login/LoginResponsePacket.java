package fr.alessdevita.valtherion.network.packet.login;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;

/**
 * Paquet de réponse de connexion dans Valtherion.
 * Envoyé par le serveur au client pour répondre à une requête de connexion.
 */
public class LoginResponsePacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 1002;

    // Nom du joueur
    private String playerName;

    // ID du joueur (généré par le serveur)
    private String playerId;

    // Indique si la connexion a réussi
    private boolean success;

    // Message de réponse (ex: raison de l'échec ou confirmation de succès)
    private String message;

    // Token de session (pour les reconnexions)
    private String sessionToken;

    // Indique si le serveur prend en charge le chiffrement
    private boolean serverSupportsEncryption;

    /**
     * Constructeur du paquet de réponse de connexion.
     */
    public LoginResponsePacket() {
        super(PACKET_ID);
    }

    /**
     * Constructeur du paquet de réponse de connexion avec un nom de joueur et un flag de succès.
     * @param playerName Nom du joueur.
     * @param success true si la connexion a réussi, false sinon.
     */
    public LoginResponsePacket(String playerName, boolean success) {
        this();
        this.playerName = playerName;
        this.success = success;
        this.message = success ? "Connexion acceptée" : "Connexion refusée";
    }

    /**
     * Constructeur du paquet de réponse de connexion avec un nom de joueur, un ID, un flag de succès et un message.
     * @param playerName Nom du joueur.
     * @param playerId ID du joueur.
     * @param success true si la connexion a réussi, false sinon.
     * @param message Message de réponse.
     */
    public LoginResponsePacket(String playerName, String playerId, boolean success, String message) {
        this(playerName, success);
        this.playerId = playerId;
        this.message = message;
    }

    public LoginResponsePacket(String playerName, boolean b, String connexionAcceptee) {
        super(playerName, b, connexionAcceptee);
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire le nom du joueur
        dataStream.writeUTF(playerName != null ? playerName : "");

        // Écrire l'ID du joueur (null si la connexion a échoué)
        dataStream.writeBoolean(playerId != null);
        if (playerId != null) {
            dataStream.writeUTF(playerId);
        }

        // Écrire le flag de succès
        dataStream.writeBoolean(success);

        // Écrire le message
        dataStream.writeUTF(message != null ? message : "");

        // Écrire le token de session (null si la connexion a échoué)
        dataStream.writeBoolean(sessionToken != null);
        if (sessionToken != null) {
            dataStream.writeUTF(sessionToken);
        }

        // Écrire le support du chiffrement côté serveur
        dataStream.writeBoolean(serverSupportsEncryption);
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire le nom du joueur
        this.playerName = dataStream.readUTF();

        // Lire l'ID du joueur
        if (dataStream.readBoolean()) {
            this.playerId = dataStream.readUTF();
        }

        // Lire le flag de succès
        this.success = dataStream.readBoolean();

        // Lire le message
        this.message = dataStream.readUTF();

        // Lire le token de session
        if (dataStream.readBoolean()) {
            this.sessionToken = dataStream.readUTF();
        }

        // Lire le support du chiffrement côté serveur
        this.serverSupportsEncryption = dataStream.readBoolean();
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite la réponse de connexion
        if (success) {
            System.out.println("Connexion réussie pour : " + playerName + " (ID : " + playerId + ")");

            // Ici, tu peux initialiser le joueur côté client avec l'ID reçu
            // Exemple :
            // gameClient.setPlayerId(playerId);
            // gameClient.setSessionToken(sessionToken);

            // Activer le chiffrement si le serveur le prend en charge
            if (serverSupportsEncryption) {
                System.out.println("Chiffrement activé.");
            }
        } else {
            System.err.println("Connexion refusée : " + message);

            // Ici, tu peux afficher le message d'erreur à l'utilisateur
        }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur ne traite pas ce paquet (il l'envoie)
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public boolean isServerSupportsEncryption() {
        return serverSupportsEncryption;
    }

    public void setServerSupportsEncryption(boolean serverSupportsEncryption) {
        this.serverSupportsEncryption = serverSupportsEncryption;
    }

    /**
     * Retourne une représentation textuelle du paquet de réponse de connexion.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "LoginResponsePacket{" +
                "playerName='" + playerName + '\'' +
                ", playerId='" + playerId + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
