package fr.alessdevita.valtherion.ecs;

/**
 * Interface de base pour les systèmes dans le système ECS de Valtherion.
 * Un système contient la logique pour traiter les entités et les composants.
 */
public interface System {


    /**
     * Initialise le système.
     */
    default void init() {}

    /**
     * Met à jour le système.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    void update(float deltaTime);

    /**
     * Démarre le système.
     */
    default void start() {}

    /**
     * Arrête le système.
     */
    default void stop() {}

    /**
     * Nettoie les ressources du système.
     */
    default void cleanup() {}

    /**
     * Définit le gestionnaire ECS associé au système.
     * @param ecsManager Gestionnaire ECS.
     */
    void setECSManager(ECSManager ecsManager);

    /**
     * Récupère le gestionnaire ECS associé au système.
     * @return Gestionnaire ECS.
     */
    ECSManager getECSManager();
}