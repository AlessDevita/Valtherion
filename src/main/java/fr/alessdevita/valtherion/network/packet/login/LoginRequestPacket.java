package fr.alessdevita.valtherion.network.packet.login;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;

/**
 * Paquet de requête de connexion dans Valtherion.
 * Envoyé par le client au serveur pour demander une connexion.
 */
public class LoginRequestPacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 1001;

    // Nom du joueur
    private String playerName;

    // Version du client (ex: "1.0.0")
    private String clientVersion;

    // Mot de passe (optionnel, peut être null)
    private String password;

    // Token de session (pour les reconnexions)
    private String sessionToken;

    // Indique si le client prend en charge le chiffrement
    private boolean supportsEncryption;

    /**
     * Constructeur du paquet de requête de connexion.
     */
    public LoginRequestPacket() {
        super(PACKET_ID);
        this.supportsEncryption = true;
    }

    /**
     * Constructeur du paquet de requête de connexion avec un nom de joueur.
     * @param playerName Nom du joueur.
     */
    public LoginRequestPacket(String playerName) {
        this();
        this.playerName = playerName;
        this.clientVersion = "1.0.0"; // Version par défaut
    }

    /**
     * Constructeur du paquet de requête de connexion avec un nom de joueur et une version.
     * @param playerName Nom du joueur.
     * @param clientVersion Version du client.
     */
    public LoginRequestPacket(String playerName, String clientVersion) {
        this(playerName);
        this.clientVersion = clientVersion;
    }

    /**
     * Constructeur du paquet de requête de connexion avec un nom de joueur, une version et un mot de passe.
     * @param playerName Nom du joueur.
     * @param clientVersion Version du client.
     * @param password Mot de passe.
     */
    public LoginRequestPacket(String playerName, String clientVersion, String password) {
        this(playerName, clientVersion);
        this.password = password;
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire le nom du joueur
        dataStream.writeUTF(playerName != null ? playerName : "");

        // Écrire la version du client
        dataStream.writeUTF(clientVersion != null ? clientVersion : "");

        // Écrire le mot de passe (null si non fourni)
        dataStream.writeBoolean(password != null);
        if (password != null) {
            dataStream.writeUTF(password);
        }

        // Écrire le token de session (null si non fourni)
        dataStream.writeBoolean(sessionToken != null);
        if (sessionToken != null) {
            dataStream.writeUTF(sessionToken);
        }

        // Écrire le support du chiffrement
        dataStream.writeBoolean(supportsEncryption);
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire le nom du joueur
        this.playerName = dataStream.readUTF();

        // Lire la version du client
        this.clientVersion = dataStream.readUTF();

        // Lire le mot de passe
        if (dataStream.readBoolean()) {
            this.password = dataStream.readUTF();
        }

        // Lire le token de session
        if (dataStream.readBoolean()) {
            this.sessionToken = dataStream.readUTF();
        }

        // Lire le support du chiffrement
        this.supportsEncryption = dataStream.readBoolean();
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client ne traite pas ce paquet (il l'envoie)
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite la requête de connexion
        System.out.println("Requête de connexion reçue pour : " + playerName +
                " (version : " + clientVersion + ")");

        // Ici, tu peux ajouter la logique pour vérifier la version du client,
        // authentifier le joueur, etc.

        // Exemple : Vérifier la version du client
        if (!isVersionSupported(clientVersion)) {
            // Envoyer une réponse de refus
            LoginResponsePacket responsePacket = new LoginResponsePacket(
                    playerName,
                    null,
                    false,
                    "Version du client non supportée. Version requise : 1.0.0"
            );
            try {
                connection.sendPacket(responsePacket);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de la réponse de refus : " + e.getMessage());
            }
            return;
        }

        // Exemple : Authentifier le joueur (à adapter selon ton système)
        if (password != null && !authenticatePlayer(playerName, password)) {
            // Envoyer une réponse de refus
            LoginResponsePacket responsePacket = new LoginResponsePacket(
                    playerName,
                    null,
                    false,
                    "Nom d'utilisateur ou mot de passe incorrect"
            );
            try {
                connection.sendPacket(responsePacket);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de la réponse de refus : " + e.getMessage());
            }
            return;
        }

        // Générer un ID unique pour le joueur
        String playerId = generatePlayerId(playerName);

        // Envoyer une réponse de connexion réussie
        LoginResponsePacket responsePacket = new LoginResponsePacket(
                playerName,
                playerId,
                true,
                "Connexion acceptée"
        );
        try {
            connection.sendPacket(responsePacket);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la réponse de connexion : " + e.getMessage());
        }
    }

    /**
     * Vérifie si une version du client est supportée.
     * @param version Version à vérifier.
     * @return true si la version est supportée, false sinon.
     */
    private boolean isVersionSupported(String version) {
        // Exemple : Vérifier que la version est "1.0.0"
        return "1.0.0".equals(version);
    }

    /**
     * Authentifie un joueur.
     * @param playerName Nom du joueur.
     * @param password Mot de passe.
     * @return true si l'authentification réussit, false sinon.
     */
    private boolean authenticatePlayer(String playerName, String password) {
        // Exemple : Vérifier le nom et le mot de passe (à adapter selon ton système)
        // Dans un vrai jeu, tu utiliserais une base de données ou un service d'authentification
        return true; // Toujours vrai pour l'exemple
    }

    /**
     * Génère un ID unique pour un joueur.
     * @param playerName Nom du joueur.
     * @return ID unique du joueur.
     */
    private String generatePlayerId(String playerName) {
        return "player_" + playerName.toLowerCase().replace(" ", "_") + "_" + System.currentTimeMillis();
    }

    // --- Getters et Setters ---

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public boolean supportsEncryption() {
        return supportsEncryption;
    }

    public void setSupportsEncryption(boolean supportsEncryption) {
        this.supportsEncryption = supportsEncryption;
    }

    /**
     * Retourne une représentation textuelle du paquet de requête de connexion.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "LoginRequestPacket{" +
                "playerName='" + playerName + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                ", supportsEncryption=" + supportsEncryption +
                '}';
    }
}