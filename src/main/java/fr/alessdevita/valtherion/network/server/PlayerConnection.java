package fr.alessdevita.valtherion.network.server;

import fr.alessdevita.valtherion.network.client.ServerConnection;
import fr.alessdevita.valtherion.network.exceptions.NetworkException;

import java.net.Socket;
import java.util.*;

/**
 * Connexion joueur dans Valtherion.
 * Représente une connexion entre le serveur et un joueur, avec des informations supplémentaires sur le joueur.
 */
public class PlayerConnection extends ServerConnection {

    // Niveau du joueur
    private int level;

    // Expérience du joueur
    private long experience;

    // Santé du joueur
    private float health;

    // Mana du joueur
    private float mana;

    // Liste des quêtes en cours
    private final List<String> activeQuests;

    // Liste des permissions du joueur
    private final Set<String> permissions;

    // Position du joueur (x, y, z)
    private float x, y, z;

    // Rotation du joueur (rx, ry, rz)
    private float rx, ry, rz;

    // Indique si le joueur est en mode créatif
    private boolean isCreativeMode;

    // Indique si le joueur est invisible
    private boolean isInvisible;

    /**
     * Constructeur de la connexion joueur.
     * @param socket Socket de la connexion.
     * @throws NetworkException Si une erreur se produit lors de l'initialisation.
     */
    public PlayerConnection(Socket socket) throws NetworkException {
        super(socket);
        this.level = 1;
        this.experience = 0;
        this.health = 100.0f;
        this.mana = 100.0f;
        this.activeQuests = new ArrayList<>();
        this.permissions = new HashSet<>();
        this.isCreativeMode = false;
        this.isInvisible = false;
    }

    /**
     * Met à jour la position du joueur.
     * @param x Position X.
     * @param y Position Y.
     * @param z Position Z.
     */
    public void updatePosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        updateLastPlayerActivity();
    }

    /**
     * Met à jour la rotation du joueur.
     * @param rx Rotation X.
     * @param ry Rotation Y.
     * @param rz Rotation Z.
     */
    public void updateRotation(float rx, float ry, float rz) {
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        updateLastPlayerActivity();
    }

    /**
     * Ajoute une quête en cours.
     * @param questId ID de la quête.
     */
    public void addActiveQuest(String questId) {
        if (!activeQuests.contains(questId)) {
            activeQuests.add(questId);
        }
    }

    /**
     * Retire une quête en cours.
     * @param questId ID de la quête.
     */
    public void removeActiveQuest(String questId) {
        activeQuests.remove(questId);
    }

    /**
     * Ajoute une permission au joueur.
     * @param permission Permission à ajouter.
     */
    public void addPermission(String permission) {
        permissions.add(permission);
    }

    /**
     * Retire une permission au joueur.
     * @param permission Permission à retirer.
     */
    public void removePermission(String permission) {
        permissions.remove(permission);
    }

    /**
     * Vérifie si le joueur a une permission.
     * @param permission Permission à vérifier.
     * @return true si le joueur a la permission, false sinon.
     */
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    /**
     * Ajoute de l'expérience au joueur.
     * @param amount Quantité d'expérience à ajouter.
     */
    public void addExperience(long amount) {
        this.experience += amount;

        // Vérifier si le joueur monte de niveau
        while (experience >= getExperienceForNextLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
    }

    /**
     * Récupère l'expérience nécessaire pour le niveau suivant.
     * @return Expérience nécessaire.
     */
    private long getExperienceForNextLevel() {
        return level * 1000L; // Exemple : 1000 XP par niveau
    }

    /**
     * Inflige des dégâts au joueur.
     * @param damage Quantité de dégâts.
     */
    public void damage(float damage) {
        this.health = Math.max(0, health - damage);
        if (health <= 0) {
            // Le joueur est mort
            System.out.println("Le joueur " + getPlayerName() + " est mort !");
        }
    }

    /**
     * Soigne le joueur.
     * @param amount Quantité de soins.
     */
    public void heal(float amount) {
        this.health = Math.min(100, health + amount);
    }

    /**
     * Utilise du mana.
     * @param amount Quantité de mana à utiliser.
     */
    public void useMana(float amount) {
        this.mana = Math.max(0, mana - amount);
    }

    /**
     * Restaure du mana.
     * @param amount Quantité de mana à restaurer.
     */
    public void restoreMana(float amount) {
        this.mana = Math.min(100, mana + amount);
    }

    // --- Getters et Setters ---

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = Math.max(0, experience);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(100, health));
    }

    public float getMana() {
        return mana;
    }

    public void setMana(float mana) {
        this.mana = Math.max(0, Math.min(100, mana));
    }

    public List<String> getActiveQuests() {
        return activeQuests;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public boolean isCreativeMode() {
        return isCreativeMode;
    }

    public void setCreativeMode(boolean isCreativeMode) {
        this.isCreativeMode = isCreativeMode;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    /**
     * Retourne une représentation textuelle de la connexion joueur.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "PlayerConnection{" +
                "playerId='" + getPlayerId() + '\'' +
                ", playerName='" + getPlayerName() + '\'' +
                ", level=" + level +
                ", health=" + health +
                ", mana=" + mana +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", isCreativeMode=" + isCreativeMode +
                ", isInvisible=" + isInvisible +
                '}';
    }
}