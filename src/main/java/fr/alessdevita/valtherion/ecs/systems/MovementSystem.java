package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;
import fr.alessdevita.valtherion.ecs.components.PhysicsComponent;
import fr.alessdevita.valtherion.ecs.components.MovementComponent;

import org.joml.Vector3f;

import java.util.*;

/**
 * Système pour gérer le mouvement des entités dans Valtherion.
 */
public class MovementSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Liste des entités avec un composant de mouvement
    private List<Entity> movableEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    /**
     * Constructeur du système de mouvement.
     */
    public MovementSystem() {
        this.movableEntities = new ArrayList<>();
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

        // Récupérer les entités avec les composants Transform, Physics et Movement
        movableEntities = ecsManager.getEntitiesWithComponents(
                TransformComponent.class,
                PhysicsComponent.class,
                MovementComponent.class
        );

        // Mettre à jour le mouvement de chaque entité
        for (Entity entity : movableEntities) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);
            MovementComponent movement = entity.getComponent(MovementComponent.class);

            if (transform != null && physics != null && movement != null) {
                updateEntityMovement(entity, transform, physics, movement, deltaTime);
            }
        }
    }

    /**
     * Met à jour le mouvement d'une entité.
     * @param entity Entité à mettre à jour.
     * @param transform Composant de transformation.
     * @param physics Composant physique.
     * @param movement Composant de mouvement.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour.
     */
    private void updateEntityMovement(Entity entity, TransformComponent transform,
                                      PhysicsComponent physics, MovementComponent movement,
                                      float deltaTime) {
        // Récupérer la vélocité actuelle
        Vector3f velocity = physics.getVelocity();

        // Appliquer les mouvements demandés
        if (movement.isMovingForward()) {
            Vector3f forward = new Vector3f(0, 0, -1).rotateY((float) Math.toRadians(transform.getRotationY()));
            velocity.add(new Vector3f(forward).mul(movement.getSpeed() * deltaTime));
        }
        if (movement.isMovingBackward()) {
            Vector3f backward = new Vector3f(0, 0, 1).rotateY((float) Math.toRadians(transform.getRotationY()));
            velocity.add(new Vector3f(backward).mul(movement.getSpeed() * deltaTime));
        }
        if (movement.isMovingLeft()) {
            Vector3f left = new Vector3f(-1, 0, 0).rotateY((float) Math.toRadians(transform.getRotationY()));
            velocity.add(new Vector3f(left).mul(movement.getSpeed() * deltaTime));
        }
        if (movement.isMovingRight()) {
            Vector3f right = new Vector3f(1, 0, 0).rotateY((float) Math.toRadians(transform.getRotationY()));
            velocity.add(new Vector3f(right).mul(movement.getSpeed() * deltaTime));
        }
        if (movement.isMovingUp()) {
            velocity.y += movement.getSpeed() * deltaTime;
        }
        if (movement.isMovingDown()) {
            velocity.y -= movement.getSpeed() * deltaTime;
        }

        // Appliquer la vélocité au corps physique
        physics.setVelocity(velocity);

        // Appliquer la rotation
        if (movement.isRotatingLeft()) {
            transform.rotate(0, -movement.getRotationSpeed() * deltaTime, 0);
        }
        if (movement.isRotatingRight()) {
            transform.rotate(0, movement.getRotationSpeed() * deltaTime, 0);
        }
        if (movement.isRotatingUp()) {
            transform.rotate(-movement.getRotationSpeed() * deltaTime, 0, 0);
        }
        if (movement.isRotatingDown()) {
            transform.rotate(movement.getRotationSpeed() * deltaTime, 0, 0);
        }

        // Réinitialiser les états de mouvement
        movement.resetMovementStates();
    }

    /**
     * Déplace une entité dans une direction.
     * @param entityId ID de l'entité.
     * @param direction Direction du mouvement.
     * @param speed Vitesse du mouvement.
     */
    public void moveEntity(String entityId, Vector3f direction, float speed) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent((Class<? extends Component>) MovementComponent.class)) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);
            movement.setMovementDirection(direction);
            movement.setSpeed(speed);
        }
    }

    /**
     * Fait tourner une entité.
     * @param entityId ID de l'entité.
     * @param rotation Rotation à appliquer (en degrés).
     */
    public void rotateEntity(String entityId, Vector3f rotation) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent((Class<? extends Component>) MovementComponent.class)) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);
            movement.setRotation(rotation);
        }
    }

    // --- Getters et Setters ---

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getMovableEntities() {
        return movableEntities;
    }
}