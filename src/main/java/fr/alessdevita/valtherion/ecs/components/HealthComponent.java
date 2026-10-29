package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;

/**
 * Composant pour la santé d'une entité dans Valtherion.
 * Stocke les points de vie, l'armure, et les états de santé.
 */
public class HealthComponent implements Component {

    // Points de vie actuels
    private float health;

    // Points de vie maximaux
    private float maxHealth;

    // Armure (réduit les dégâts)
    private float armor;

    // Indique si l'entité est invulnérable
    private boolean isInvulnerable;

    // Temps d'invulnérabilité restant (en secondes)
    private float invulnerabilityTime;

    // Indique si l'entité est en vie
    private boolean isAlive;

    // Entité associée
    private Entity entity;

    // Dernière entité qui a infligé des dégâts
    private Entity lastDamager;

    // Temps depuis le dernier dégât (en secondes)
    private float timeSinceLastDamage;

    /**
     * Constructeur du composant de santé.
     */
    public HealthComponent() {
        this.health = 100.0f;
        this.maxHealth = 100.0f;
        this.armor = 0.0f;
        this.isInvulnerable = false;
        this.invulnerabilityTime = 0.0f;
        this.isAlive = true;
        this.lastDamager = null;
        this.timeSinceLastDamage = 0.0f;
    }

    /**
     * Constructeur du composant de santé avec des points de vie.
     * @param health Points de vie initiaux.
     * @param maxHealth Points de vie maximaux.
     */
    public HealthComponent(float health, float maxHealth) {
        this();
        this.health = health;
        this.maxHealth = maxHealth;
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
        // Mettre à jour le temps d'invulnérabilité
        if (isInvulnerable) {
            invulnerabilityTime -= deltaTime;
            if (invulnerabilityTime <= 0) {
                isInvulnerable = false;
            }
        }

        // Mettre à jour le temps depuis le dernier dégât
        timeSinceLastDamage += deltaTime;
    }

    /**
     * Inflige des dégâts à l'entité.
     * @param damage Montant des dégâts.
     * @param damager Entité qui inflige les dégâts.
     */
    public void damage(float damage, Entity damager) {
        if (!isAlive || isInvulnerable) {
            return;
        }

        // Réduire les dégâts en fonction de l'armure
        float reducedDamage = Math.max(1, damage - armor);

        // Appliquer les dégâts
        health -= reducedDamage;

        // Mettre à jour le dernier infligeur de dégâts
        lastDamager = damager;
        timeSinceLastDamage = 0.0f;

        // Vérifier si l'entité est morte
        if (health <= 0) {
            health = 0;
            isAlive = false;
            onDeath();
        }
    }

    /**
     * Soigne l'entité.
     * @param amount Montant de soins.
     */
    public void heal(float amount) {
        if (!isAlive) {
            return;
        }

        health = Math.min(health + amount, maxHealth);
    }

    /**
     * Appelé lorsque l'entité meurt.
     */
    private void onDeath() {
        java.lang.System.out.println("Entité " + entity.getId() + " est morte.");
    }

    /**
     * Rend l'entité invulnérable pendant une durée.
     * @param duration Durée d'invulnérabilité (en secondes).
     */
    public void setInvulnerable(float duration) {
        isInvulnerable = true;
        invulnerabilityTime = duration;
    }

    /**
     * Réinitialise les points de vie.
     */
    public void resetHealth() {
        health = maxHealth;
    }

    // --- Getters et Setters ---

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = Math.min(health, maxHealth);
    }

    public float getArmor() {
        return armor;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Entity getLastDamager() {
        return lastDamager;
    }

    public float getTimeSinceLastDamage() {
        return timeSinceLastDamage;
    }

    /**
     * Retourne le pourcentage de santé.
     * @return Pourcentage de santé (0.0 à 1.0).
     */
    public float getHealthPercentage() {
        return health / maxHealth;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "HealthComponent{" +
                "health=" + health +
                "/" + maxHealth +
                ", armor=" + armor +
                ", isAlive=" + isAlive +
                '}';
    }
}