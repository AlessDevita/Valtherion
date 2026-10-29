package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;
import fr.alessdevita.valtherion.ecs.components.AIComponent;
import fr.alessdevita.valtherion.ecs.components.MovementComponent;

import org.joml.Vector3f;

import java.util.*;

/**
 * Système pour gérer l'intelligence artificielle des entités dans Valtherion.
 */
public class AISystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Liste des entités avec un composant d'IA
    private List<Entity> aiEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    /**
     * Constructeur du système d'IA.
     */
    public AISystem() {
        this.aiEntities = new ArrayList<>();
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

        // Récupérer les entités avec les composants AI, Transform, et Movement
        aiEntities = ecsManager.getEntitiesWithComponents(
                AIComponent.class,
                TransformComponent.class,
                MovementComponent.class
        );

        // Mettre à jour l'IA de chaque entité
        for (Entity entity : aiEntities) {
            updateEntityAI(entity, deltaTime);
        }
    }

    /**
     * Met à jour l'IA d'une entité.
     * @param entity Entité à mettre à jour.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour.
     */
    private void updateEntityAI(Entity entity, float deltaTime) {
        AIComponent aiComponent = entity.getComponent(AIComponent.class);
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        MovementComponent movementComponent = entity.getComponent(MovementComponent.class);

        if (aiComponent == null || transformComponent == null || movementComponent == null) {
            return;
        }

        // Mettre à jour l'IA en fonction de son comportement
        switch (aiComponent.getBehavior()) {
            case PASSIVE:
                updatePassiveAI(aiComponent, movementComponent);
                break;
            case WANDER:
                updateWanderAI(entity, aiComponent, transformComponent, movementComponent, deltaTime);
                break;
            case FOLLOW:
                updateFollowAI(entity, aiComponent, transformComponent, movementComponent);
                break;
            case ATTACK:
                updateAttackAI(entity, aiComponent, transformComponent, movementComponent);
                break;
            case FLEE:
                updateFleeAI(entity, aiComponent, transformComponent, movementComponent);
                break;
            case PATROL:
                updatePatrolAI(entity, aiComponent, transformComponent, movementComponent, deltaTime);
                break;
            case GUARD:
                updateGuardAI(entity, aiComponent, transformComponent, movementComponent);
                break;
        }
    }

    /**
     * Met à jour une IA passive.
     * @param aiComponent Composant d'IA.
     * @param movementComponent Composant de mouvement.
     */
    private void updatePassiveAI(AIComponent aiComponent, MovementComponent movementComponent) {
        // Ne rien faire
        movementComponent.resetMovementStates();
    }

    /**
     * Met à jour une IA qui erre aléatoirement.
     * @param entity Entité.
     * @param aiComponent Composant d'IA.
     * @param transformComponent Composant de transformation.
     * @param movementComponent Composant de mouvement.
     * @param deltaTime Temps écoulé.
     */
    private void updateWanderAI(Entity entity, AIComponent aiComponent,
                                TransformComponent transformComponent,
                                MovementComponent movementComponent,
                                float deltaTime) {
        // Changer de direction aléatoirement
        if (Math.random() < 0.01) { // 1% de chance de changer de direction
            float newX = (float) (Math.random() * 2 - 1); // -1 à 1
            float newZ = (float) (Math.random() * 2 - 1); // -1 à 1

            // Normaliser le vecteur de direction
            Vector3f direction = new Vector3f(newX, 0, newZ).normalize();

            // Définir la direction de mouvement
            movementComponent.setMovementDirection(direction);
            movementComponent.setMovingForward(true);
        }
    }

    /**
     * Met à jour une IA qui suit une cible.
     * @param entity Entité.
     * @param aiComponent Composant d'IA.
     * @param transformComponent Composant de transformation.
     * @param movementComponent Composant de mouvement.
     */
    private void updateFollowAI(Entity entity, AIComponent aiComponent,
                                TransformComponent transformComponent,
                                MovementComponent movementComponent) {
        Entity target = aiComponent.getTarget();
        if (target == null || !target.hasComponent(TransformComponent.class)) {
            movementComponent.resetMovementStates();
            return;
        }

        TransformComponent targetTransform = target.getComponent(TransformComponent.class);

        // Calculer la direction vers la cible
        Vector3f direction = new Vector3f(
                targetTransform.getX() - transformComponent.getX(),
                0,
                targetTransform.getZ() - transformComponent.getZ()
        ).normalize();

        // Définir la direction de mouvement
        movementComponent.setMovementDirection(direction);
        movementComponent.setMovingForward(true);

        // Faire tourner l'entité vers la cible
        float angle = (float) Math.toDegrees(Math.atan2(direction.z, direction.x));
        float currentAngle = transformComponent.getRotationY();
        float angleDiff = angle - currentAngle;

        // Normaliser l'angle
        while (angleDiff > 180) {
            angleDiff -= 360;
        }
        while (angleDiff < -180) {
            angleDiff += 360;
        }

        if (angleDiff > 5) {
            movementComponent.setRotatingRight(true);
        } else if (angleDiff < -5) {
            movementComponent.setRotatingLeft(true);
        }
    }

    /**
     * Met à jour une IA qui attaque une cible.
     * @param entity Entité.
     * @param aiComponent Composant d'IA.
     * @param transformComponent Composant de transformation.
     * @param movementComponent Composant de mouvement.
     */
    private void updateAttackAI(Entity entity, AIComponent aiComponent,
                                TransformComponent transformComponent,
                                MovementComponent movementComponent) {
        Entity target = aiComponent.getTarget();
        if (target == null || !target.hasComponent(TransformComponent.class)) {
            movementComponent.resetMovementStates();
            return;
        }

        TransformComponent targetTransform = target.getComponent(TransformComponent.class);

        // Vérifier si la cible est dans la portée d'attaque
        if (aiComponent.isTargetInAttackRange(target)) {
            // Attaquer la cible
            if (aiComponent.canAttack()) {
                // Logique pour attaquer la cible
                java.lang.System.out.println(entity.getId() + " attaque " + target.getId());
                aiComponent.resetAttackCooldown();
            }
            movementComponent.resetMovementStates();
        } else {
            // Suivre la cible
            updateFollowAI(entity, aiComponent, transformComponent, movementComponent);
        }
    }

    /**
     * Met à jour une IA qui fuit une cible.
     * @param entity Entité.
     * @param aiComponent Composant d'IA.
     * @param transformComponent Composant de transformation.
     * @param movementComponent Composant de mouvement.
     */
    private void updateFleeAI(Entity entity, AIComponent aiComponent,
                              TransformComponent transformComponent,
                              MovementComponent movementComponent) {
        Entity target = aiComponent.getTarget();
        if (target == null || !target.hasComponent(TransformComponent.class)) {
            movementComponent.resetMovementStates();
            return;
        }

        TransformComponent targetTransform = target.getComponent(TransformComponent.class);

        // Calculer la direction opposée à la cible
        Vector3f direction = new Vector3f(
                transformComponent.getX() - targetTransform.getX(),
                0,
                transformComponent.getZ() - targetTransform.getZ()
        ).normalize();

        // Définir la direction de mouvement
        movementComponent.setMovementDirection(direction);
        movementComponent.setMovingForward(true);
    }

    /**
     * Met à jour une IA qui patrouille entre des points.
     * @param entity Entité.
     * @param aiComponent Composant d'IA.
     * @param transformComponent Composant de transformation.
     * @param movementComponent Composant de mouvement.
     * @param deltaTime Temps écoulé.
     */
    private void updatePatrolAI(Entity entity, AIComponent aiComponent,
                                TransformComponent transformComponent,
                                MovementComponent movementComponent,
                                float deltaTime) {
        // Logique pour patrouiller entre des points
        // Exemple : Utiliser une liste de points de patrouille
    }

    /**
     * Met à jour une IA qui garde une position.
     * @param entity Entité.
     * @param aiComponent Composant d'IA.
     * @param transformComponent Composant de transformation.
     * @param movementComponent Composant de mouvement.
     */
    private void updateGuardAI(Entity entity, AIComponent aiComponent,
                               TransformComponent transformComponent,
                               MovementComponent movementComponent) {
        // Logique pour garder une position
        // Exemple : Vérifier si une cible est dans la portée de détection
        // Si oui, passer en mode ATTACK ou FOLLOW
    }

    // --- Getters et Setters ---

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getAiEntities() {
        return aiEntities;
    }
}