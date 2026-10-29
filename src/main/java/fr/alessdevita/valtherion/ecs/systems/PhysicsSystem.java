package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;
import fr.alessdevita.valtherion.ecs.components.PhysicsComponent;
import fr.alessdevita.valtherion.physics.PhysicsEngine;
import fr.alessdevita.valtherion.physics.Forces;

import org.joml.Vector3f;

import java.util.*;

/**
 * Système pour gérer la physique des entités dans Valtherion.
 */
public class PhysicsSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Moteur physique associé
    private PhysicsEngine physicsEngine;

    // Gravité du monde
    private Vector3f gravity;

    // Liste des entités avec un composant physique
    private List<Entity> physicsEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    /**
     * Constructeur du système de physique.
     * @param physicsEngine Moteur physique associé.
     */
    public PhysicsSystem(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
        this.gravity = physicsEngine != null ? physicsEngine.getGravity() : new Vector3f(0, -9.81f, 0);
        this.physicsEntities = new ArrayList<>();
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
    public void init() {
        if (physicsEngine != null) {
            physicsEngine.init(null); // À adapter selon ton implémentation
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null || physicsEngine == null) {
            return;
        }

        // Récupérer les entités avec les composants Transform et Physics
        physicsEntities = ecsManager.getEntitiesWithComponents(
                TransformComponent.class,
                PhysicsComponent.class
        );

        // Mettre à jour le moteur physique
        physicsEngine.update(deltaTime);

        // Mettre à jour les composants physiques des entités
        for (Entity entity : physicsEntities) {
            PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

            if (physicsComponent != null && transformComponent != null) {
                // Synchroniser la position du corps physique avec celle de l'entité
                Vector3f position = new Vector3f(
                        transformComponent.getX(),
                        transformComponent.getY(),
                        transformComponent.getZ()
                );
                physicsComponent.getCollider().setPosition(position);

                // Appliquer la gravité si nécessaire
                if (physicsComponent.isAffectedByGravity()) {
                    Forces.applyGravity(physicsComponent.getPhysicsBody(), gravity);
                }

                // Synchroniser la position de l'entité avec celle du corps physique
                Vector3f physicsPosition = physicsComponent.getPhysicsBody().getEntity().getPosition();
                transformComponent.setPosition(physicsPosition);
            }
        }
    }

    @Override
    public void cleanup() {
        if (physicsEngine != null) {
            physicsEngine.cleanup();
        }
        physicsEntities.clear();
    }

    /**
     * Applique une force à une entité.
     * @param entityId ID de l'entité.
     * @param force Force à appliquer.
     */
    public void applyForce(String entityId, Vector3f force) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(PhysicsComponent.class)) {
            PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
            physicsComponent.applyForce(force);
        }
    }

    /**
     * Applique une impulsion à une entité.
     * @param entityId ID de l'entité.
     * @param impulse Impulsion à appliquer.
     */
    public void applyImpulse(String entityId, Vector3f impulse) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(PhysicsComponent.class)) {
            PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
            physicsComponent.applyImpulse(impulse);
        }
    }

    // --- Getters et Setters ---

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
        if (physicsEngine != null) {
            physicsEngine.setGravity(gravity);
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getPhysicsEntities() {
        return physicsEntities;
    }
}