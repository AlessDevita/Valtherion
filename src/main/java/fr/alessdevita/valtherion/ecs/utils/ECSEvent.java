package fr.alessdevita.valtherion.ecs.utils;

import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.Component;

/**
 * Classe pour les événements ECS dans Valtherion.
 * Représente les événements liés aux entités, composants, et systèmes.
 */
public class ECSEvent {

    // Type de l'événement
    private final ECSEventType type;

    // Entité associée à l'événement
    private Entity entity;

    // Composant associé à l'événement
    private Component component;

    // Données supplémentaires de l'événement
    private Object data;

    // Temps de l'événement (en secondes)
    private float time;

    // Énumération des types d'événements ECS
    public enum ECSEventType {
        // Événements d'entité
        ENTITY_CREATED,          // Une entité a été créée
        ENTITY_DESTROYED,        // Une entité a été détruite
        ENTITY_ENABLED,          // Une entité a été activée
        ENTITY_DISABLED,         // Une entité a été désactivée

        // Événements de composant
        COMPONENT_ADDED,         // Un composant a été ajouté à une entité
        COMPONENT_REMOVED,       // Un composant a été retiré d'une entité
        COMPONENT_UPDATED,       // Un composant a été mis à jour

        // Événements de système
        SYSTEM_STARTED,          // Un système a été démarré
        SYSTEM_STOPPED,          // Un système a été arrêté
        SYSTEM_UPDATED,          // Un système a été mis à jour

        // Événements personnalisés
        CUSTOM_EVENT             // Événement personnalisé
    }

    /**
     * Constructeur de l'événement ECS.
     * @param type Type de l'événement.
     */
    public ECSEvent(ECSEventType type) {
        this.type = type;
        this.entity = null;
        this.component = null;
        this.data = null;
        this.time = 0.0f;
    }

    /**
     * Constructeur de l'événement ECS avec une entité.
     * @param type Type de l'événement.
     * @param entity Entité associée.
     */
    public ECSEvent(ECSEventType type, Entity entity) {
        this(type);
        this.entity = entity;
    }

    /**
     * Constructeur de l'événement ECS avec une entité et un composant.
     * @param type Type de l'événement.
     * @param entity Entité associée.
     * @param component Composant associé.
     */
    public ECSEvent(ECSEventType type, Entity entity, Component component) {
        this(type, entity);
        this.component = component;
    }

    /**
     * Constructeur de l'événement ECS avec une entité, un composant, et des données.
     * @param type Type de l'événement.
     * @param entity Entité associée.
     * @param component Composant associé.
     * @param data Données supplémentaires.
     */
    public ECSEvent(ECSEventType type, Entity entity, Component component, Object data) {
        this(type, entity, component);
        this.data = data;
    }

    // --- Méthodes statiques pour créer des événements courants ---

    /**
     * Crée un événement de création d'entité.
     * @param entity Entité créée.
     * @return Événement de création d'entité.
     */
    public static ECSEvent createEntityCreatedEvent(Entity entity) {
        return new ECSEvent(ECSEventType.ENTITY_CREATED, entity);
    }

    /**
     * Crée un événement de destruction d'entité.
     * @param entity Entité détruite.
     * @return Événement de destruction d'entité.
     */
    public static ECSEvent createEntityDestroyedEvent(Entity entity) {
        return new ECSEvent(ECSEventType.ENTITY_DESTROYED, entity);
    }

    /**
     * Crée un événement d'ajout de composant.
     * @param entity Entité associée.
     * @param component Composant ajouté.
     * @return Événement d'ajout de composant.
     */
    public static ECSEvent createComponentAddedEvent(Entity entity, Component component) {
        return new ECSEvent(ECSEventType.COMPONENT_ADDED, entity, component);
    }

    /**
     * Crée un événement de retrait de composant.
     * @param entity Entité associée.
     * @param component Composant retiré.
     * @return Événement de retrait de composant.
     */
    public static ECSEvent createComponentRemovedEvent(Entity entity, Component component) {
        return new ECSEvent(ECSEventType.COMPONENT_REMOVED, entity, component);
    }

    /**
     * Crée un événement de mise à jour de composant.
     * @param entity Entité associée.
     * @param component Composant mis à jour.
     * @return Événement de mise à jour de composant.
     */
    public static ECSEvent createComponentUpdatedEvent(Entity entity, Component component) {
        return new ECSEvent(ECSEventType.COMPONENT_UPDATED, entity, component);
    }

    /**
     * Crée un événement personnalisé.
     * @param type Type de l'événement.
     * @param entity Entité associée.
     * @param data Données supplémentaires.
     * @return Événement personnalisé.
     */
    public static ECSEvent createCustomEvent(ECSEventType type, Entity entity, Object data) {
        return new ECSEvent(type, entity, null, data);
    }

    // --- Getters et Setters ---

    public ECSEventType getType() {
        return type;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    /**
     * Retourne une représentation textuelle de l'événement ECS.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ECSEvent{" +
                "type=" + type +
                ", entity=" + (entity != null ? entity.getId() : "null") +
                ", component=" + (component != null ? component.getClass().getSimpleName() : "null") +
                ", data=" + data +
                '}';
    }
}