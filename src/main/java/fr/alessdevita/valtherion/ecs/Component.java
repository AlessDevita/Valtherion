package fr.alessdevita.valtherion.ecs;

/**
 * Interface de base pour les composants dans le système ECS de Valtherion.
 * Un composant représente une donnée ou un état associé à une entité.
 */
public interface Component {

    /**
     * Initialise le composant.
     */
    default void init() {}

    /**
     * Met à jour le composant.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    default void update(float deltaTime) {}

    /**
     * Nettoie les ressources du composant.
     */
    default void cleanup() {}

    /**
     * Définit l'entité associée au composant.
     * @param entity Entité associée.
     */
    void setEntity(Entity entity);

    /**
     * Récupère l'entité associée au composant.
     * @return Entité associée.
     */
    Entity getEntity();
}