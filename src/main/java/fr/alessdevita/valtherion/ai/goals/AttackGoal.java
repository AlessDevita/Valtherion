package fr.alessdevita.valtherion.ai.goals;

import fr.alessdevita.valtherion.entity.living.LivingEntity;

/**
 * Objectif d'attaque pour les entités IA.
 * Permet à une entité d'attaquer une cible (joueur, mob, etc.) lorsqu'elle est à portée.
 */
public class AttackGoal extends Goal {

    // Cible à attaquer
    private LivingEntity target;

    // Portée d'attaque (distance maximale pour attaquer)
    private float attackRange;

    // Dégâts infligés par l'attaque
    private int attackDamage;

    // Cooldown entre les attaques (en ticks)
    private int attackCooldown;
    private int cooldownCounter;

    // Indique si l'entité est en train d'attaquer
    private boolean isAttacking;

    /**
     * Constructeur de l'objectif d'attaque.
     * @param entity Entité qui attaque.
     * @param target Cible à attaquer.
     * @param attackRange Portée d'attaque.
     * @param attackDamage Dégâts infligés par l'attaque.
     * @param attackCooldown Cooldown entre les attaques (en ticks).
     * @param priority Priorité de l'objectif.
     */
    public AttackGoal(
            LivingEntity entity,
            LivingEntity target,
            float attackRange,
            int attackDamage,
            int attackCooldown,
            int priority
    ) {
        super(
                "Attack " + (target != null ? target.getClass().getSimpleName() : "null"),
                entity,
                priority
        );
        this.target = target;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
        this.cooldownCounter = 0;
        this.isAttacking = false;
    }

    /**
     * Appelé lorsque l'objectif est activé.
     */
    @Override
    protected void onActivate() {
        logger.debug(
                entity.getClass().getSimpleName() + " commence à attaquer " +
                        (target != null ? target.getClass().getSimpleName() : "null")
        );
        isAttacking = false;
        cooldownCounter = 0;
    }

    /**
     * Appelé lorsque l'objectif est désactivé.
     */
    @Override
    protected void onDeactivate() {
        logger.debug(
                entity.getClass().getSimpleName() + " arrête d'attaquer " +
                        (target != null ? target.getClass().getSimpleName() : "null")
        );
        isAttacking = false;
        cooldownCounter = 0;
    }

    /**
     * Met à jour l'objectif.
     * @return Nouveau statut de l'objectif.
     */
    @Override
    protected Status onUpdate() {
        // Vérifier si la cible est valide
        if (target == null || !target.isAlive()) {
            logger.debug("Cible invalide ou morte. Objectif échoué.");
            return Status.FAILED;
        }

        // Calculer la distance entre l'entité et la cible
        float distanceToTarget = (float) entity.getDistanceTo(target);

        // Si la cible est hors de portée, l'objectif ne peut pas être accompli
        if (distanceToTarget > attackRange) {
            return Status.INACTIVE; // L'objectif reste inactif jusqu'à ce que la cible soit à portée
        }

        // Vérifier le cooldown
        if (cooldownCounter > 0) {
            cooldownCounter--;
            return Status.ACTIVE; // Toujours en cours, mais en cooldown
        }

        // Si on n'est pas en train d'attaquer, lancer une attaque
        if (!isAttacking) {
            isAttacking = true;
            attackTarget();
            cooldownCounter = attackCooldown;
        }

        // L'objectif reste actif tant que la cible est à portée
        return Status.ACTIVE;
    }

    /**
     * Vérifie si l'objectif peut être activé.
     * @return true si l'objectif peut être activé.
     */
    @Override
    protected boolean onCanActivate() {
        return target != null && target.isAlive();
    }

    /**
     * Réinitialise l'objectif.
     */
    @Override
    protected void onReset() {
        isAttacking = false;
        cooldownCounter = 0;
    }

    /**
     * Attaque la cible.
     */
    private void attackTarget() {
        if (target == null || !target.isAlive()) {
            return;
        }

        // Infliger des dégâts à la cible
        target.damage(attackDamage);

        // Log de l'attaque
        logger.debug(
                entity.getClass().getSimpleName() + " attaque " +
                        target.getClass().getSimpleName() + " pour " + attackDamage + " dégâts."
        );

        // Réinitialiser l'état d'attaque
        isAttacking = false;
    }

    // --- Getters et Setters ---

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

    public boolean isAttacking() {
        return isAttacking;
    }
}
