package fr.alessdevita.valtherion.save;

import java.util.*;

/**
 * Classe pour stocker les données de sauvegarde dans Valtherion.
 * Contient toutes les informations nécessaires pour sauvegarder et charger l'état du jeu.
 */
public class SaveData {

    // Version de la sauvegarde (pour la compatibilité)
    private int version;

    // Nom de la sauvegarde
    private String saveName;

    // Date de la sauvegarde (en millisecondes)
    private long timestamp;

    // Données des entités
    private final Map<String, Object> entities;

    // Données des joueurs
    private final Map<String, Object> players;

    // Données du monde
    private Object worldData;

    // Données des quêtes
    private final Map<String, Object> quests;

    // Données des inventaires
    private final Map<String, Object> inventories;

    // Données personnalisées
    private final Map<String, Object> customData;

    // Métadonnées de la sauvegarde
    private final Map<String, String> metadata;

    /**
     * Constructeur des données de sauvegarde.
     */
    public SaveData() {
        this.version = 1;
        this.saveName = "New Save";
        this.timestamp = System.currentTimeMillis();
        this.entities = new HashMap<>();
        this.players = new HashMap<>();
        this.quests = new HashMap<>();
        this.inventories = new HashMap<>();
        this.customData = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Constructeur des données de sauvegarde avec un nom.
     * @param saveName Nom de la sauvegarde.
     */
    public SaveData(String saveName) {
        this();
        this.saveName = saveName;
    }

    /**
     * Ajoute les données d'une entité.
     * @param entityId ID de l'entité.
     * @param data Données de l'entité.
     */
    public void addEntityData(String entityId, Object data) {
        entities.put(entityId, data);
    }

    /**
     * Récupère les données d'une entité.
     * @param entityId ID de l'entité.
     * @return Données de l'entité ou null si non trouvées.
     */
    public Object getEntityData(String entityId) {
        return entities.get(entityId);
    }

    /**
     * Ajoute les données d'un joueur.
     * @param playerId ID du joueur.
     * @param data Données du joueur.
     */
    public void addPlayerData(String playerId, Object data) {
        players.put(playerId, data);
    }

    /**
     * Récupère les données d'un joueur.
     * @param playerId ID du joueur.
     * @return Données du joueur ou null si non trouvées.
     */
    public Object getPlayerData(String playerId) {
        return players.get(playerId);
    }

    /**
     * Définit les données du monde.
     * @param worldData Données du monde.
     */
    public void setWorldData(Object worldData) {
        this.worldData = worldData;
    }

    /**
     * Récupère les données du monde.
     * @return Données du monde.
     */
    public Object getWorldData() {
        return worldData;
    }

    /**
     * Ajoute les données d'une quête.
     * @param questId ID de la quête.
     * @param data Données de la quête.
     */
    public void addQuestData(String questId, Object data) {
        quests.put(questId, data);
    }

    /**
     * Récupère les données d'une quête.
     * @param questId ID de la quête.
     * @return Données de la quête ou null si non trouvées.
     */
    public Object getQuestData(String questId) {
        return quests.get(questId);
    }

    /**
     * Ajoute les données d'un inventaire.
     * @param inventoryId ID de l'inventaire.
     * @param data Données de l'inventaire.
     */
    public void addInventoryData(String inventoryId, Object data) {
        inventories.put(inventoryId, data);
    }

    /**
     * Récupère les données d'un inventaire.
     * @param inventoryId ID de l'inventaire.
     * @return Données de l'inventaire ou null si non trouvées.
     */
    public Object getInventoryData(String inventoryId) {
        return inventories.get(inventoryId);
    }

    /**
     * Ajoute une donnée personnalisée.
     * @param key Clé de la donnée.
     * @param value Valeur de la donnée.
     */
    public void addCustomData(String key, Object value) {
        customData.put(key, value);
    }

    /**
     * Récupère une donnée personnalisée.
     * @param key Clé de la donnée.
     * @return Valeur de la donnée ou null si non trouvée.
     */
    public Object getCustomData(String key) {
        return customData.get(key);
    }

    /**
     * Ajoute une métadonnée.
     * @param key Clé de la métadonnée.
     * @param value Valeur de la métadonnée.
     */
    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    /**
     * Récupère une métadonnée.
     * @param key Clé de la métadonnée.
     * @return Valeur de la métadonnée ou null si non trouvée.
     */
    public String getMetadata(String key) {
        return metadata.get(key);
    }

    // --- Getters et Setters ---

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getEntities() {
        return entities;
    }

    public Map<String, Object> getPlayers() {
        return players;
    }

    public Map<String, Object> getQuests() {
        return quests;
    }

    public Map<String, Object> getInventories() {
        return inventories;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Nettoie les données de sauvegarde.
     */
    public void clear() {
        entities.clear();
        players.clear();
        quests.clear();
        inventories.clear();
        customData.clear();
        metadata.clear();
    }

    /**
     * Retourne une représentation textuelle des données de sauvegarde.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SaveData{" +
                "saveName='" + saveName + '\'' +
                ", timestamp=" + timestamp +
                ", entities=" + entities.size() +
                ", players=" + players.size() +
                ", quests=" + quests.size() +
                ", inventories=" + inventories.size() +
                '}';
    }
}