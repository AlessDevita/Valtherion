package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;

import java.util.*;

/**
 * Composant pour l'intelligence artificielle d'une entité dans Valtherion.
 * Stocke les comportements, les cibles, et les états de l'IA.
 */
public class AIComponent implements Component {

    // Type de comportement de l'IA
    private AIBehavior behavior;

    // Cible de l'IA (entité ou position)
    private Entity target;

    // Position cible (si pas d'entité cible)
    private float targetX, targetY, targetZ;

    // Distance de détection
    private float detectionRange;

    // Distance d'attaque
    private float attackRange;

    // Temps entre les attaques (en secondes)
    private float attackCooldown;

    // Temps restant avant la prochaine attaque (en secondes)
    private float attackCooldownRemaining;

    // Vitesse de déplacement de l'IA
    private float moveSpeed;

    // Vitesse de rotation de l'IA
    private float rotationSpeed;

    // État actuel de l'IA
    private AIState state;

    // Liste des états possibles de l'IA
    private final Map<String, AIState> states;

    // État par défaut de l'IA
    private AIState defaultState;

    // Entité associée
    private Entity entity;

    // Énumération des types de comportement de l'IA
    public enum AIBehavior {
        PASSIVE,      // Ne fait rien
        WANDER,       // Erre aléatoirement
        FOLLOW,       // Suit une cible
        ATTACK,       // Attaque une cible
        FLEE,         // Fuit une cible
        PATROL,       // Patrouille entre des points
        GUARD         // Garde une position
    }

    // Énumération des états de l'IA
    public enum AIState {
        IDLE,         // Inactif
        CHASING,      // Poursuite
        ATTACKING,    // Attaque
        FLEEING,      // Fuite
        PATROLLING,   // Patrouille
        SEARCHING,    // Recherche
        DEAD          // Mort
    }

    // Classe pour représenter un état de l'IA
    public static class AIStateConfig {
        private final String name;
        private final AIBehavior behavior;
        private final float duration;

        public AIStateConfig(String name, AIBehavior behavior, float duration) {
            this.name = name;
            this.behavior = behavior;
            this.duration = duration;
        }

        public String getName() {
            return name;
        }

        public AIBehavior getBehavior() {
            return behavior;
        }

        public float getDuration() {
            return duration;
        }
    }

    /**
     * Constructeur du composant d'IA.
     */
    public AIComponent() {
        this.behavior = AIBehavior.PASSIVE;
        this.target = null;
        this.targetX = 0;
        this.targetY = 0;
        this.targetZ = 0;
        this.detectionRange = 10.0f;
        this.attackRange = 2.0f;
        this.attackCooldown = 1.0f;
        this.attackCooldownRemaining = 0.0f;
        this.moveSpeed = 2.0f;
        this.rotationSpeed = 90.0f;
        this.state = AIState.IDLE;
        this.states = new HashMap<>();
        this.defaultState = AIState.IDLE;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void update(float deltaTime) {
        // Mettre à jour le cooldown d'attaque
        if (attackCooldownRemaining > 0) {
            attackCooldownRemaining -= deltaTime;
        }
    }

    /**
     * Définit la cible de l'IA.
     * @param target Entité cible.
     */
    public void setTarget(Entity target) {
        this.target = target;
        if (target != null) {
            this.targetX = (float) target.getComponent(TransformComponent.class).getX();
            this.targetY = (float) target.getComponent(TransformComponent.class).getY();
            this.targetZ = (float) target.getComponent(TransformComponent.class).getZ();
        }
    }

    /**
     * Définit la position cible de l'IA.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void setTargetPosition(float x, float y, float z) {
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
        this.target = null;
    }

    /**
     * Vérifie si une cible est dans la portée de détection.
     * @param target Entité à vérifier.
     * @return true si la cible est dans la portée de détection, false sinon.
     */
    public boolean isTargetInDetectionRange(Entity target) {
        if (target == null || !target.hasComponent(TransformComponent.class)) {
            return false;
        }

        TransformComponent targetTransform = target.getComponent(TransformComponent.class);
        TransformComponent entityTransform = entity.getComponent(TransformComponent.class);

        float distance = (float) Math.sqrt(
                Math.pow(targetTransform.getX() - entityTransform.getX(), 2) +
                        Math.pow(targetTransform.getY() - entityTransform.getY(), 2) +
                        Math.pow(targetTransform.getZ() - entityTransform.getZ(), 2)
        );

        return distance <= detectionRange;
    }

    /**
     * Vérifie si une cible est dans la portée d'attaque.
     * @param target Entité à vérifier.
     * @return true si la cible est dans la portée d'attaque, false sinon.
     */
    public boolean isTargetInAttackRange(Entity target) {
        if (target == null || !target.hasComponent(TransformComponent.class)) {
            return false;
        }

        TransformComponent targetTransform = target.getComponent(TransformComponent.class);
        TransformComponent entityTransform = entity.getComponent(TransformComponent.class);

        float distance = (float) Math.sqrt(
                Math.pow(targetTransform.getX() - entityTransform.getX(), 2) +
                        Math.pow(targetTransform.getY() - entityTransform.getY(), 2) +
                        Math.pow(targetTransform.getZ() - entityTransform.getZ(), 2)
        );

        return distance <= attackRange;
    }

    /**
     * Peut attaquer.
     * @return true si l'IA peut attaquer, false sinon.
     */
    public boolean canAttack() {
        return attackCooldownRemaining <= 0;
    }

    /**
     * Réinitialise le cooldown d'attaque.
     */
    public void resetAttackCooldown() {
        attackCooldownRemaining = attackCooldown;
    }

    // --- Getters et Setters ---

    public AIBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(AIBehavior behavior) {
        this.behavior = behavior;
    }

    public Entity getTarget() {
        return target;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetZ() {
        return targetZ;
    }

    public float getDetectionRange() {
        return detectionRange;
    }

    public void setDetectionRange(float detectionRange) {
        this.detectionRange = detectionRange;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(float attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public AIState getState() {
        return state;
    }

    public void setState(AIState state) {
        this.state = state;
    }

    public Map<String, AIState> getStates() {
        return states;
    }

    public AIState getDefaultState() {
        return defaultState;
    }

    public void setDefaultState(AIState defaultState) {
        this.defaultState = defaultState;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "AIComponent{" +
                "behavior=" + behavior +
                ", state=" + state +
                ", target=" + (target != null ? target.getId() : "null") +
                ", detectionRange=" + detectionRange +
                '}';
    }
}