package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;

import java.util.*;

/**
 * Composant pour la synchronisation réseau d'une entité dans Valtherion.
 * Stocke les propriétés nécessaires pour synchroniser l'entité entre les clients et le serveur.
 */
public class NetworkComponent implements Component {

    // ID réseau unique de l'entité
    private String networkId;

    // Type de synchronisation (FULL, PARTIAL, NONE)
    private SyncType syncType;

    // Fréquence de synchronisation (en Hz)
    private float syncFrequency;

    // Temps écoulé depuis la dernière synchronisation (en secondes)
    private float timeSinceLastSync;

    // Indique si l'entité est synchronisée
    private boolean isSynced;

    // Indique si l'entité est contrôlée par le client local
    private boolean isLocal;

    // Propriétaire de l'entité (ID du joueur ou du serveur)
    private String ownerId;

    // Liste des composants à synchroniser
    private final List<Class<? extends Component>> syncedComponents;

    // Données à synchroniser (pour les mises à jour partielles)
    private final Map<String, Object> syncData;

    // Entité associée
    private Entity entity;

    // Énumération des types de synchronisation
    public enum SyncType {
        FULL,      // Synchronisation complète (tous les composants)
        PARTIAL,   // Synchronisation partielle (composants sélectionnés)
        NONE       // Pas de synchronisation
    }

    /**
     * Constructeur du composant réseau.
     */
    public NetworkComponent() {
        this.networkId = UUID.randomUUID().toString();
        this.syncType = SyncType.FULL;
        this.syncFrequency = 20.0f; // 20 Hz
        this.timeSinceLastSync = 0.0f;
        this.isSynced = false;
        this.isLocal = false;
        this.ownerId = "";
        this.syncedComponents = new ArrayList<>();
        this.syncData = new HashMap<>();
    }

    /**
     * Constructeur du composant réseau avec un ID réseau.
     * @param networkId ID réseau unique.
     */
    public NetworkComponent(String networkId) {
        this();
        this.networkId = networkId;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void update(float deltaTime) {
        // Mettre à jour le temps depuis la dernière synchronisation
        timeSinceLastSync += deltaTime;

        // Vérifier si une synchronisation est nécessaire
        if (timeSinceLastSync >= 1.0f / syncFrequency) {
            timeSinceLastSync = 0.0f;
            sync();
        }
    }

    /**
     * Synchronise l'entité.
     */
    public void sync() {
        if (!isSynced || entity == null) {
            return;
        }

        // Réinitialiser les données de synchronisation
        syncData.clear();

        // Synchroniser les composants sélectionnés
        for (Class<? extends Component> componentClass : syncedComponents) {
            if (entity.hasComponent(componentClass)) {
                Component component = entity.getComponent(componentClass);
                // Logique pour sérialiser le composant
                // Exemple : syncData.put(componentClass.getSimpleName(), serializeComponent(component));
            }
        }

        // Envoyer les données de synchronisation
        // Exemple : NetworkManager.sendSyncData(networkId, syncData);

        isSynced = true;
    }

    /**
     * Ajoute un composant à synchroniser.
     * @param componentClass Classe du composant.
     */
    public void addSyncedComponent(Class<? extends Component> componentClass) {
        if (componentClass != null && !syncedComponents.contains(componentClass)) {
            syncedComponents.add(componentClass);
        }
    }

    /**
     * Retire un composant à synchroniser.
     * @param componentClass Classe du composant.
     */
    public void removeSyncedComponent(Class<? extends Component> componentClass) {
        syncedComponents.remove(componentClass);
    }

    /**
     * Définit les données de synchronisation.
     * @param key Clé des données.
     * @param value Valeur des données.
     */
    public void setSyncData(String key, Object value) {
        syncData.put(key, value);
    }

    /**
     * Récupère les données de synchronisation.
     * @param key Clé des données.
     * @return Valeur des données ou null si non trouvée.
     */
    public Object getSyncData(String key) {
        return syncData.get(key);
    }

    // --- Getters et Setters ---

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public SyncType getSyncType() {
        return syncType;
    }

    public void setSyncType(SyncType syncType) {
        this.syncType = syncType;
    }

    public float getSyncFrequency() {
        return syncFrequency;
    }

    public void setSyncFrequency(float syncFrequency) {
        this.syncFrequency = syncFrequency;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Class<? extends Component>> getSyncedComponents() {
        return syncedComponents;
    }

    public Map<String, Object> getSyncData() {
        return syncData;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "NetworkComponent{" +
                "networkId='" + networkId + '\'' +
                ", syncType=" + syncType +
                ", syncFrequency=" + syncFrequency +
                ", isSynced=" + isSynced +
                ", isLocal=" + isLocal +
                '}';
    }
}