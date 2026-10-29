package fr.alessdevita.valtherion.ai.behavior.behaviors;

import fr.alessdevita.valtherion.ai.behavior.Behavior;
import fr.alessdevita.valtherion.ai.behavior.LeafBehavior;
import fr.alessdevita.valtherion.ai.goals.AttackGoal;
import fr.alessdevita.valtherion.ai.goals.Goal;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

/**
 * Comportement d'attaque pour les arbres de comportement.
 * Permet à une entité d'attaquer une cible en utilisant un objectif d'attaque (AttackGoal).
 */
public class AttackBehavior extends LeafBehavior {

    // Objectif d'attaque associé à ce comportement
    private AttackGoal attackGoal;

    // Cible à attaquer
    private LivingEntity target;

    // Portée d'attaque
    private float attackRange;

    // Dégâts infligés par l'attaque
    private int attackDamage;

    // Cooldown entre les attaques (en ticks)
    private int attackCooldown;

    /**
     * Constructeur du comportement d'attaque.
     * @param target Cible à attaquer.
     * @param attackRange Portée d'attaque.
     * @param attackDamage Dégâts infligés par l'attaque.
     * @param attackCooldown Cooldown entre les attaques (en ticks).
     */
    public AttackBehavior(
            LivingEntity target,
            float attackRange,
            int attackDamage,
            int attackCooldown
    ) {
        super("Attack " + (target != null ? target.getClass().getSimpleName() : "null"));
        this.target = target;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
    }

    /**
     * Exécute le comportement pour une entité.
     * @param entity Entité associée au comportement.
     * @return État du comportement (SUCCESS, FAILURE, ou RUNNING).
     */
    @Override
    public Behavior.Status execute(LivingEntity entity) {
        // Vérifier si l'entité ou la cible est nulle
        if (entity == null || target == null || !target.isAlive()) {
            return Behavior.Status.FAILURE;
        }

        // Si l'objectif d'attaque n'existe pas encore, le créer
        if (attackGoal == null || attackGoal.getEntity() != entity) {
            attackGoal = new AttackGoal(
                    entity,
                    target,
                    attackRange,
                    attackDamage,
                    attackCooldown,
                    0 // Priorité (non utilisée ici, car gérée par le BehaviorTree)
            );
        }

        // Calculer la distance entre l'entité et la cible
        float distanceToTarget = (float) entity.getDistanceTo(target);

        // Si la cible est à portée, activer l'objectif d'attaque
        if (distanceToTarget <= attackRange) {
            if (attackGoal.getStatus() != Goal.Status.ACTIVE) {
                attackGoal.activate();
            }
            attackGoal.update();

            // Si l'objectif est accompli, retourner SUCCESS
            if (attackGoal.getStatus() == Goal.Status.COMPLETED) {
                return Behavior.Status.SUCCESS;
            }

            // Si l'objectif a échoué, retourner FAILURE
            if (attackGoal.getStatus() == Goal.Status.FAILED) {
                return Behavior.Status.FAILURE;
            }

            // Sinon, l'objectif est toujours en cours
            return Behavior.Status.RUNNING;
        }

        // Si la cible est hors de portée, désactiver l'objectif
        if (attackGoal != null && attackGoal.getStatus() == Goal.Status.ACTIVE) {
            attackGoal.deactivate();
        }

        // Retourner FAILURE si la cible est hors de portée
        return Behavior.Status.FAILURE;
    }

    // --- Getters et Setters ---

    public AttackGoal getAttackGoal() {
        return attackGoal;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }
}
