package fr.alessdevita.valtherion.save;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.components.*;
import fr.alessdevita.valtherion.save.exceptions.SaveException;

import java.util.*;

/**
 * Gestionnaire principal des sauvegardes dans Valtherion.
 * Centralise la sauvegarde et le chargement des données du jeu.
 */
public class SaveManager {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Gestionnaire de sauvegarde utilisé (fichier, base de données, etc.)
    private SaveHandler saveHandler;

    // Sérialiseur utilisé pour convertir les objets
    private final Serializer serializer;

    // Liste des observateurs de sauvegarde
    private final List<SaveListener> listeners;

    // Indique si le gestionnaire est activé
    private boolean isEnabled;

    /**
     * Constructeur du gestionnaire de sauvegarde.
     */
    public SaveManager() {
        this.serializer = new Serializer();
        this.listeners = new ArrayList<>();
        this.isEnabled = true;
    }

    /**
     * Constructeur du gestionnaire de sauvegarde avec un gestionnaire de sauvegarde.
     * @param saveHandler Gestionnaire de sauvegarde à utiliser.
     */
    public SaveManager(SaveHandler saveHandler) {
        this();
        this.saveHandler = saveHandler;
    }

    /**
     * Initialise le gestionnaire de sauvegarde.
     * @param ecsManager Gestionnaire ECS associé.
     */
    public void init(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
        if (saveHandler != null) {
            saveHandler.init(ecsManager);
        }
    }

    /**
     * Sauvegarde l'état actuel du jeu.
     * @param saveName Nom de la sauvegarde.
     * @throws SaveException Si une erreur se produit pendant la sauvegarde.
     */
    public void saveGame(String saveName) throws SaveException {
        if (!isEnabled || ecsManager == null || saveHandler == null) {
            throw new SaveException("Le gestionnaire de sauvegarde n'est pas activé ou initialisé.");
        }

        // Créer les données de sauvegarde
        SaveData saveData = new SaveData(saveName);

        // Sauvegarder les entités
        saveEntities(saveData);

        // Sauvegarder les joueurs
        savePlayers(saveData);

        // Sauvegarder le monde
        saveWorld(saveData);

        // Sauvegarder les quêtes
        saveQuests(saveData);

        // Sauvegarder les inventaires
        saveInventories(saveData);

        // Ajouter des métadonnées
        saveData.addMetadata("game_version", "1.0.0");
        saveData.addMetadata("author", "AlessDevita");

        // Sauvegarder les données
        saveHandler.save(saveData);

        // Notifier les observateurs
        notifyListeners(saveName, true);
    }

    /**
     * Sauvegarde les entités.
     * @param saveData Données de sauvegarde.
     */
    private void saveEntities(SaveData saveData) {
        for (Entity entity : ecsManager.getAllEntities()) {
            // Sérialiser l'entité
            Object entityData = serializeEntity(entity);
            if (entityData != null) {
                saveData.addEntityData(entity.getId(), entityData);
            }
        }
    }

    /**
     * Sérialise une entité.
     * @param entity Entité à sérialiser.
     * @return Données sérialisées de l'entité.
     */
    private Object serializeEntity(Entity entity) {
        if (entity == null) {
            return null;
        }

        Map<String, Object> entityData = new HashMap<>();

        // Sérialiser les composants de l'entité
        for (Component component : entity.getAllComponents()) {
            if (component instanceof Saveable) {
                Saveable saveable = (Saveable) component;
                entityData.put(component.getClass().getSimpleName(), saveable.serialize());
            } else {
                // Sérialiser le composant avec Gson
                entityData.put(component.getClass().getSimpleName(), serializer.serializeToMap(component));
            }
        }

        // Ajouter les tags de l'entité
        entityData.put("tags", new ArrayList<>(entity.getTags()));

        return entityData;
    }

    /**
     * Sauvegarde les joueurs.
     * @param saveData Données de sauvegarde.
     */
    private void savePlayers(SaveData saveData) {
        // Exemple : Sauvegarder les joueurs avec un composant PlayerComponent
        // À adapter selon ton implémentation
    }

    /**
     * Sauvegarde le monde.
     * @param saveData Données de sauvegarde.
     */
    private void saveWorld(SaveData saveData) {
        // Exemple : Sauvegarder le monde
        // À adapter selon ton implémentation
    }

    /**
     * Sauvegarde les quêtes.
     * @param saveData Données de sauvegarde.
     */
    private void saveQuests(SaveData saveData) {
        // Exemple : Sauvegarder les quêtes des joueurs
        // À adapter selon ton implémentation
    }

    /**
     * Sauvegarde les inventaires.
     * @param saveData Données de sauvegarde.
     */
    private void saveInventories(SaveData saveData) {
        // Exemple : Sauvegarder les inventaires des joueurs
        // À adapter selon ton implémentation
    }

    /**
     * Charge une sauvegarde.
     * @param saveName Nom de la sauvegarde.
     * @throws SaveException Si une erreur se produit pendant le chargement.
     */
    public void loadGame(String saveName) throws SaveException {
        if (!isEnabled || ecsManager == null || saveHandler == null) {
            throw new SaveException("Le gestionnaire de sauvegarde n'est pas activé ou initialisé.");
        }

        // Charger les données de sauvegarde
        SaveData saveData = saveHandler.load(saveName);

        // Charger les entités
        loadEntities(saveData);

        // Charger les joueurs
        loadPlayers(saveData);

        // Charger le monde
        loadWorld(saveData);

        // Charger les quêtes
        loadQuests(saveData);

        // Charger les inventaires
        loadInventories(saveData);

        // Notifier les observateurs
        notifyListeners(saveName, false);
    }

    /**
     * Charge les entités.
     * @param saveData Données de sauvegarde.
     */
    private void loadEntities(SaveData saveData) {
        for (Map.Entry<String, Object> entry : saveData.getEntities().entrySet()) {
            String entityId = entry.getKey();
            Object entityData = entry.getValue();

            // Créer une nouvelle entité
            Entity entity = ecsManager.createEntity(entityId);

            // Désérialiser les composants de l'entité
            deserializeEntity(entity, entityData);
        }
    }

    /**
     * Désérialise une entité.
     * @param entity Entité à désérialiser.
     * @param entityData Données sérialisées de l'entité.
     */
    private void deserializeEntity(Entity entity, Object entityData) {
        if (entity == null || entityData == null) {
            return;
        }

        // Convertir les données en Map si nécessaire
        Map<String, Object> dataMap = entityData instanceof Map ? (Map<String, Object>) entityData : serializer.serializeToMap(entityData);

        // Désérialiser les composants
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String componentName = entry.getKey();
            Object componentData = entry.getValue();

            // Trouver la classe du composant
            Class<? extends Component> componentClass = findComponentClass(componentName);
            if (componentClass != null) {
                try {
                    // Créer une instance du composant
                    Component component = componentClass.getDeclaredConstructor().newInstance();

                    // Désérialiser le composant
                    if (component instanceof Saveable) {
                        Saveable saveable = (Saveable) component;
                        saveable.deserialize(componentData);
                    } else {
                        // Désérialiser le composant avec Gson
                        serializer.deserializeFromMap((Map<String, Object>) componentData, componentClass);
                    }

                    // Ajouter le composant à l'entité
                    entity.addComponent(component);
                } catch (Exception e) {
                    System.err.println("Impossible de désérialiser le composant " + componentName + " : " + e.getMessage());
                }
            }
        }

        // Désérialiser les tags de l'entité
        if (dataMap.containsKey("tags")) {
            Object tagsData = dataMap.get("tags");
            if (tagsData instanceof List) {
                for (Object tag : (List<?>) tagsData) {
                    entity.addTag(tag.toString());
                }
            }
        }
    }

    /**
     * Trouve la classe d'un composant en fonction de son nom.
     * @param componentName Nom du composant.
     * @return Classe du composant ou null si non trouvée.
     */
    private Class<? extends Component> findComponentClass(String componentName) {
        // Map des noms de composants vers leurs classes
        Map<String, Class<? extends Component>> componentClasses = new HashMap<>();
        componentClasses.put("TransformComponent", TransformComponent.class);
        componentClasses.put("RenderComponent", RenderComponent.class);
        componentClasses.put("PhysicsComponent", PhysicsComponent.class);
        componentClasses.put("MovementComponent", MovementComponent.class);
        componentClasses.put("HealthComponent", HealthComponent.class);
        componentClasses.put("AIComponent", AIComponent.class);
        componentClasses.put("InventoryComponent", InventoryComponent.class);
        componentClasses.put("CollisionComponent", CollisionComponent.class);
        componentClasses.put("QuestComponent", QuestComponent.class);

        return componentClasses.get(componentName);
    }

    /**
     * Charge les joueurs.
     * @param saveData Données de sauvegarde.
     */
    private void loadPlayers(SaveData saveData) {
        // Exemple : Charger les joueurs
        // À adapter selon ton implémentation
    }

    /**
     * Charge le monde.
     * @param saveData Données de sauvegarde.
     */
    private void loadWorld(SaveData saveData) {
        // Exemple : Charger le monde
        // À adapter selon ton implémentation
    }

    /**
     * Charge les quêtes.
     * @param saveData Données de sauvegarde.
     */
    private void loadQuests(SaveData saveData) {
        // Exemple : Charger les quêtes
        // À adapter selon ton implémentation
    }

    /**
     * Charge les inventaires.
     * @param saveData Données de sauvegarde.
     */
    private void loadInventories(SaveData saveData) {
        // Exemple : Charger les inventaires
        // À adapter selon ton implémentation
    }

    /**
     * Notifie les observateurs de sauvegarde.
     * @param saveName Nom de la sauvegarde.
     * @param isSave true si c'est une sauvegarde, false si c'est un chargement.
     */
    private void notifyListeners(String saveName, boolean isSave) {
        for (SaveListener listener : listeners) {
            if (isSave) {
                listener.onSave(saveName);
            } else {
                listener.onLoad(saveName);
            }
        }
    }

    /**
     * Ajoute un observateur de sauvegarde.
     * @param listener Observateur à ajouter.
     */
    public void addSaveListener(SaveListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur de sauvegarde.
     * @param listener Observateur à retirer.
     */
    public void removeSaveListener(SaveListener listener) {
        listeners.remove(listener);
    }

    // --- Getters et Setters ---

    public ECSManager getECSManager() {
        return ecsManager;
    }

    public void setECSManager(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
    }

    public SaveHandler getSaveHandler() {
        return saveHandler;
    }

    public void setSaveHandler(SaveHandler saveHandler) {
        this.saveHandler = saveHandler;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    // --- Interface pour les observateurs de sauvegarde ---
    public interface SaveListener {
        void onSave(String saveName);
        void onLoad(String saveName);
    }
}