package fr.alessdevita.valtherion.ecs;

import fr.alessdevita.valtherion.ecs.components.QuestComponent;

import java.util.*;

/**
 * Gestionnaire principal du système ECS (Entity-Component-System) pour Valtherion.
 * Centralise la création, la gestion et la mise à jour des entités, composants et systèmes.
 */
public class ECSManager {

    // Map des entités (ID -> Entité)
    private final Map<String, Entity> entities;

    // Liste des systèmes
    private final List<System> systems;

    // Compteur pour les IDs des entités
    private int nextEntityId;

    // Indique si le gestionnaire est activé
    private boolean isRunning;

    /**
     * Constructeur du gestionnaire ECS.
     */
    public ECSManager() {
        this.entities = new HashMap<>();
        this.systems = new ArrayList<>();
        this.nextEntityId = 0;
        this.isRunning = false;
    }

    /**
     * Initialise le gestionnaire ECS.
     */
    public void init() {
        isRunning = true;
        java.lang.System.out.println("Gestionnaire ECS initialisé.");
    }

    /**
     * Met à jour le gestionnaire ECS.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        if (!isRunning) {
            return;
        }

        // Mettre à jour tous les systèmes
        for (System system : systems) {
            system.update(deltaTime);
        }
    }

    /**
     * Crée une nouvelle entité.
     * @return Nouvelle entité.
     */
    public Entity createEntity() {
        String entityId = "entity_" + nextEntityId++;
        Entity entity = new Entity(entityId);
        entities.put(entityId, entity);
        return entity;
    }

    /**
     * Crée une nouvelle entité avec un ID spécifique.
     * @param entityId ID de l'entité.
     * @return Nouvelle entité.
     */
    public Entity createEntity(String entityId) {
        if (entities.containsKey(entityId)) {
            throw new IllegalArgumentException("Une entité avec l'ID " + entityId + " existe déjà.");
        }
        Entity entity = new Entity(entityId);
        entities.put(entityId, entity);
        return entity;
    }

    /**
     * Détruit une entité.
     * @param entityId ID de l'entité à détruire.
     */
    public void destroyEntity(String entityId) {
        Entity entity = entities.get(entityId);
        if (entity != null) {
            entity.destroy();
            entities.remove(entityId);
        }
    }

    /**
     * Détruit une entité.
     * @param entity Entité à détruire.
     */
    public void destroyEntity(Entity entity) {
        if (entity != null) {
            destroyEntity(entity.getId());
        }
    }

    /**
     * Récupère une entité par son ID.
     * @param entityId ID de l'entité.
     * @return Entité ou null si non trouvée.
     */
    public Entity getEntity(String entityId) {
        return entities.get(entityId);
    }

    /**
     * Vérifie si une entité existe.
     * @param entityId ID de l'entité.
     * @return true si l'entité existe, false sinon.
     */
    public boolean hasEntity(String entityId) {
        return entities.containsKey(entityId);
    }

    /**
     * Ajoute un système au gestionnaire.
     * @param system Système à ajouter.
     */
    public void addSystem(System system) {
        if (system != null && !systems.contains(system)) {
            systems.add(system);
            system.setECSManager(this);
        }
    }

    /**
     * Retire un système du gestionnaire.
     * @param system Système à retirer.
     */
    public void removeSystem(System system) {
        systems.remove(system);
    }

    /**
     * Récupère un système par son type.
     * @param systemClass Classe du système.
     * @return Système ou null si non trouvé.
     */
    public <T extends System> T getSystem(Class<T> systemClass) {
        for (System system : systems) {
            if (systemClass.isInstance(system)) {
                return systemClass.cast(system);
            }
        }
        return null;
    }

    /**
     * Récupère toutes les entités.
     * @return Collection des entités.
     */
    public Collection<Entity> getAllEntities() {
        return entities.values();
    }

    /**
     * Récupère toutes les entités avec un composant spécifique.
     * @param componentClass Classe du composant.
     * @return Liste des entités avec le composant.
     */
    public <T extends Component> List<Entity> getEntitiesWithComponent(Class<T> componentClass) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : entities.values()) {
            if (entity.hasComponent(componentClass)) {
                result.add(entity);
            }
        }
        return result;
    }

    /**
     * Récupère toutes les entités avec plusieurs composants.
     * @param componentClasses Classes des composants.
     * @return Liste des entités avec tous les composants.
     */
    public List<Entity> getEntitiesWithComponents(Class<? extends Component>... componentClasses) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : entities.values()) {
            boolean hasAllComponents = true;
            for (Class<? extends Component> componentClass : componentClasses) {
                if (!entity.hasComponent(componentClass)) {
                    hasAllComponents = false;
                    break;
                }
            }
            if (hasAllComponents) {
                result.add(entity);
            }
        }
        return result;
    }

    /**
     * Démarre le gestionnaire ECS.
     */
    public void start() {
        isRunning = true;
        for (System system : systems) {
            system.start();
        }
    }

    /**
     * Arrête le gestionnaire ECS.
     */
    public void stop() {
        isRunning = false;
        for (System system : systems) {
            system.stop();
        }
    }

    /**
     * Nettoie les ressources du gestionnaire ECS.
     */
    public void cleanup() {
        stop();

        // Détruire toutes les entités
        for (Entity entity : entities.values()) {
            entity.destroy();
        }
        entities.clear();

        // Nettoyer tous les systèmes
        for (System system : systems) {
            system.cleanup();
        }
        systems.clear();

        java.lang.System.out.println("Gestionnaire ECS nettoyé.");
    }

    // --- Getters et Setters ---

    public boolean isRunning() {
        return isRunning;
    }

    public int getEntityCount() {
        return entities.size();
    }

    public int getSystemCount() {
        return systems.size();
    }
}