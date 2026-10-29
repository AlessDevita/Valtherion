package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.AnimationComponent;
import fr.alessdevita.valtherion.ecs.components.AnimationComponent.Animation;

import java.util.*;

/**
 * Système pour gérer les animations des entités dans Valtherion.
 */
public class AnimationSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Liste des entités avec un composant d'animation
    private List<Entity> animationEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    /**
     * Constructeur du système d'animation.
     */
    public AnimationSystem() {
        this.animationEntities = new ArrayList<>();
        this.isEnabled = true;
    }

    @Override
    public void setECSManager(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
    }

    @Override
    public ECSManager getECSManager() {
        return ecsManager;
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null) {
            return;
        }

        // Récupérer les entités avec le composant Animation
        animationEntities = ecsManager.getEntitiesWithComponent(AnimationComponent.class);

        // Mettre à jour les animations de chaque entité
        for (Entity entity : animationEntities) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            if (animationComponent != null) {
                animationComponent.update(deltaTime);
            }
        }
    }

    /**
     * Joue une animation pour une entité.
     * @param entityId ID de l'entité.
     * @param animationName Nom de l'animation.
     */
    public void playAnimation(String entityId, String animationName) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            animationComponent.playAnimation(animationName);
        }
    }

    /**
     * Arrête l'animation actuelle pour une entité.
     * @param entityId ID de l'entité.
     */
    public void stopAnimation(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            animationComponent.stopAnimation();
        }
    }

    /**
     * Met en pause l'animation actuelle pour une entité.
     * @param entityId ID de l'entité.
     */
    public void pauseAnimation(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            animationComponent.pauseAnimation();
        }
    }

    /**
     * Reprend l'animation actuelle pour une entité.
     * @param entityId ID de l'entité.
     */
    public void resumeAnimation(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            animationComponent.resumeAnimation();
        }
    }

    /**
     * Ajoute une animation à une entité.
     * @param entityId ID de l'entité.
     * @param name Nom de l'animation.
     * @param animation Animation à ajouter.
     */
    public void addAnimation(String entityId, String name, Animation animation) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            animationComponent.addAnimation(name, animation);
        }
    }

    /**
     * Récupère la frame actuelle de l'animation pour une entité.
     * @param entityId ID de l'entité.
     * @return Indice de la frame actuelle.
     */
    public int getCurrentFrame(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(AnimationComponent.class)) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            return animationComponent.getCurrentFrame();
        }
        return 0;
    }

    // --- Getters et Setters ---

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getAnimationEntities() {
        return animationEntities;
    }
}