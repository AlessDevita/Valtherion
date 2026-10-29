package fr.alessdevita.valtherion.ecs.utils;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.ECSManager;

import java.util.*;

/**
 * Classe pour mapper les composants des entités dans Valtherion.
 * Permet un accès rapide aux composants sans avoir à parcourir toutes les entités.
 */
public class ComponentMapper<T extends Component> {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Classe du composant
    private final Class<T> componentClass;

    // Map des entités avec ce composant (ID de l'entité -> composant)
    private final Map<String, T> entityComponentMap;

    // Liste des entités avec ce composant
    private final List<Entity> entitiesWithComponent;

    // Indique si le mappeur est activé
    private boolean isEnabled;

    /**
     * Constructeur du mappeur de composants.
     * @param componentClass Classe du composant.
     */
    public ComponentMapper(Class<T> componentClass) {
        this.componentClass = componentClass;
        this.entityComponentMap = new HashMap<>();
        this.entitiesWithComponent = new ArrayList<>();
        this.isEnabled = true;
    }

    /**
     * Initialise le mappeur de composants.
     * @param ecsManager Gestionnaire ECS associé.
     */
    public void init(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
        if (ecsManager != null) {
            updateEntityList();
        }
    }

    /**
     * Met à jour la liste des entités avec ce composant.
     */
    public void updateEntityList() {
        if (ecsManager == null) {
            return;
        }

        // Effacer les anciennes données
        entityComponentMap.clear();
        entitiesWithComponent.clear();

        // Parcourir toutes les entités et ajouter celles qui ont le composant
        for (Entity entity : ecsManager.getAllEntities()) {
            if (entity.hasComponent(componentClass)) {
                T component = entity.getComponent(componentClass);
                if (component != null) {
                    entityComponentMap.put(entity.getId(), component);
                    entitiesWithComponent.add(entity);
                }
            }
        }
    }

    /**
     * Récupère le composant d'une entité.
     * @param entityId ID de l'entité.
     * @return Composant ou null si non trouvé.
     */
    public T getComponent(String entityId) {
        if (!isEnabled) {
            return null;
        }
        return entityComponentMap.get(entityId);
    }

    /**
     * Récupère le composant d'une entité.
     * @param entity Entité.
     * @return Composant ou null si non trouvé.
     */
    public T getComponent(Entity entity) {
        if (entity == null) {
            return null;
        }
        return getComponent(entity.getId());
    }

    /**
     * Vérifie si une entité a ce composant.
     * @param entityId ID de l'entité.
     * @return true si l'entité a ce composant, false sinon.
     */
    public boolean hasComponent(String entityId) {
        return entityComponentMap.containsKey(entityId);
    }

    /**
     * Vérifie si une entité a ce composant.
     * @param entity Entité.
     * @return true si l'entité a ce composant, false sinon.
     */
    public boolean hasComponent(Entity entity) {
        if (entity == null) {
            return false;
        }
        return hasComponent(entity.getId());
    }

    /**
     * Récupère toutes les entités avec ce composant.
     * @return Liste des entités avec ce composant.
     */
    public List<Entity> getEntitiesWithComponent() {
        return entitiesWithComponent;
    }

    /**
     * Récupère tous les composants de ce type.
     * @return Collection des composants.
     */
    public Collection<T> getAllComponents() {
        return entityComponentMap.values();
    }

    /**
     * Ajoute une entité au mappeur.
     * @param entity Entité à ajouter.
     */
    public void addEntity(Entity entity) {
        if (entity == null || !entity.hasComponent(componentClass)) {
            return;
        }

        T component = entity.getComponent(componentClass);
        if (component != null) {
            entityComponentMap.put(entity.getId(), component);
            if (!entitiesWithComponent.contains(entity)) {
                entitiesWithComponent.add(entity);
            }
        }
    }

    /**
     * Retire une entité du mappeur.
     * @param entity Entité à retirer.
     */
    public void removeEntity(Entity entity) {
        if (entity == null) {
            return;
        }

        entityComponentMap.remove(entity.getId());
        entitiesWithComponent.remove(entity);
    }

    /**
     * Met à jour le composant d'une entité.
     * @param entity Entité.
     */
    public void updateEntity(Entity entity) {
        if (entity == null) {
            return;
        }

        if (entity.hasComponent(componentClass)) {
            T component = entity.getComponent(componentClass);
            if (component != null) {
                entityComponentMap.put(entity.getId(), component);
                if (!entitiesWithComponent.contains(entity)) {
                    entitiesWithComponent.add(entity);
                }
            }
        } else {
            removeEntity(entity);
        }
    }

    /**
     * Nettoie les ressources du mappeur de composants.
     */
    public void cleanup() {
        entityComponentMap.clear();
        entitiesWithComponent.clear();
    }

    // --- Getters et Setters ---

    public ECSManager getECSManager() {
        return ecsManager;
    }

    public void setECSManager(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
    }

    public Class<T> getComponentClass() {
        return componentClass;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getEntityCount() {
        return entityComponentMap.size();
    }
}