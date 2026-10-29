package fr.alessdevita.valtherion.network.security;

import fr.alessdevita.valtherion.network.utils.EncryptionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Authentificateur pour Valtherion.
 * Gère l'authentification des joueurs et la gestion des sessions.
 */
public class Authenticator {

    // Instance unique pour le singleton
    private static Authenticator instance;

    // Utilitaires de chiffrement
    private final EncryptionUtils encryptionUtils;

    // Map des joueurs (nom d'utilisateur -> mot de passe haché + sel)
    private final Map<String, PlayerCredentials> players;

    // Map des sessions actives (token de session -> nom d'utilisateur)
    private final Map<String, Session> activeSessions;

    // Durée de validité des tokens de session (en millisecondes)
    private long sessionTimeout;

    /**
     * Constructeur privé pour le singleton.
     */
    private Authenticator() {
        this.encryptionUtils = EncryptionUtils.getInstance();
        this.players = new ConcurrentHashMap<>();
        this.activeSessions = new ConcurrentHashMap<>();
        this.sessionTimeout = 3600000; // 1 heure par défaut

        // Ajouter un joueur par défaut pour les tests
        registerPlayer("admin", "admin123", "ADMIN");
        registerPlayer("Alessandro", "monMotDePasse", "PLAYER");
    }

    /**
     * Récupère l'instance unique de Authenticator.
     * @return Instance unique.
     */
    public static Authenticator getInstance() {
        if (instance == null) {
            instance = new Authenticator();
        }
        return instance;
    }

    // --- Classe interne pour les identifiants des joueurs ---

    /**
     * Identifiants d'un joueur.
     */
    private static class PlayerCredentials {
        String hashedPassword;
        String salt;
        String role;

        PlayerCredentials(String hashedPassword, String salt, String role) {
            this.hashedPassword = hashedPassword;
            this.salt = salt;
            this.role = role;
        }
    }

    // --- Classe interne pour les sessions ---

    /**
     * Session d'un joueur.
     */
    private static class Session {
        String playerName;
        String role;
        long creationTime;
        long lastActivityTime;

        Session(String playerName, String role) {
            this.playerName = playerName;
            this.role = role;
            this.creationTime = System.currentTimeMillis();
            this.lastActivityTime = creationTime;
        }

        void updateLastActivityTime() {
            this.lastActivityTime = System.currentTimeMillis();
        }

        boolean isExpired(long timeout) {
            return (System.currentTimeMillis() - lastActivityTime) > timeout;
        }
    }

    // --- Méthodes pour la gestion des joueurs ---

    /**
     * Enregistre un joueur.
     * @param username Nom d'utilisateur.
     * @param password Mot de passe.
     * @param role Rôle du joueur.
     */
    public void registerPlayer(String username, String password, String role) {
        String salt = encryptionUtils.generateSalt();
        String hashedPassword = encryptionUtils.hashPasswordWithSalt(password, salt);
        players.put(username, new PlayerCredentials(hashedPassword, salt, role));
    }

    /**
     * Supprime un joueur.
     * @param username Nom d'utilisateur.
     */
    public void unregisterPlayer(String username) {
        players.remove(username);
    }

    /**
     * Vérifie si un joueur existe.
     * @param username Nom d'utilisateur.
     * @return true si le joueur existe, false sinon.
     */
    public boolean playerExists(String username) {
        return players.containsKey(username);
    }

    // --- Méthodes pour l'authentification ---

    /**
     * Authentifie un joueur.
     * @param username Nom d'utilisateur.
     * @param password Mot de passe.
     * @return Token de session si l'authentification réussit, null sinon.
     */
    public String authenticate(String username, String password) {
        PlayerCredentials credentials = players.get(username);
        if (credentials == null) {
            return null;
        }

        String hashedPassword = encryptionUtils.hashPasswordWithSalt(password, credentials.salt);
        if (!hashedPassword.equals(credentials.hashedPassword)) {
            return null;
        }

        // Générer un token de session
        String sessionToken = generateSessionToken();

        // Créer une session
        Session session = new Session(username, credentials.role);
        activeSessions.put(sessionToken, session);

        return sessionToken;
    }

    /**
     * Valide un token de session.
     * @param sessionToken Token de session.
     * @return Nom d'utilisateur si le token est valide, null sinon.
     */
    public String validateSession(String sessionToken) {
        Session session = activeSessions.get(sessionToken);
        if (session == null) {
            return null;
        }

        // Vérifier si la session a expiré
        if (session.isExpired(sessionTimeout)) {
            activeSessions.remove(sessionToken);
            return null;
        }

        // Mettre à jour le temps de dernière activité
        session.updateLastActivityTime();

        return session.playerName;
    }

    /**
     * Récupère le rôle d'un joueur à partir d'un token de session.
     * @param sessionToken Token de session.
     * @return Rôle du joueur ou null si le token est invalide.
     */
    public String getPlayerRole(String sessionToken) {
        Session session = activeSessions.get(sessionToken);
        return session != null ? session.role : null;
    }

    /**
     * Déconnecte un joueur.
     * @param sessionToken Token de session.
     */
    public void disconnect(String sessionToken) {
        activeSessions.remove(sessionToken);
    }

    /**
     * Génère un token de session unique.
     * @return Token de session.
     */
    private String generateSessionToken() {
        return UUID.randomUUID().toString();
    }

    // --- Méthodes pour la gestion des permissions ---

    /**
     * Vérifie si un joueur a un rôle spécifique.
     * @param sessionToken Token de session.
     * @param requiredRole Rôle requis.
     * @return true si le joueur a le rôle, false sinon.
     */
    public boolean hasRole(String sessionToken, String requiredRole) {
        String playerRole = getPlayerRole(sessionToken);
        if (playerRole == null) {
            return false;
        }

        // Vérifier si le rôle du joueur est au moins égal au rôle requis
        // Exemple : ADMIN > MODERATOR > PLAYER
        return getRoleLevel(playerRole) >= getRoleLevel(requiredRole);
    }

    /**
     * Récupère le niveau d'un rôle.
     * @param role Rôle.
     * @return Niveau du rôle.
     */
    private int getRoleLevel(String role) {
        switch (role.toUpperCase()) {
            case "ADMIN":
                return 3;
            case "MODERATOR":
                return 2;
            case "PLAYER":
                return 1;
            default:
                return 0;
        }
    }

    // --- Getters et Setters ---

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * Retourne une représentation textuelle de l'authentificateur.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Authenticator{" +
                "playerCount=" + players.size() +
                ", activeSessionCount=" + activeSessions.size() +
                ", sessionTimeout=" + sessionTimeout +
                '}';
    }
}