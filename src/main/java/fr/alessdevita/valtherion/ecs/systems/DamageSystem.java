package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.HealthComponent;
import fr.alessdevita.valtherion.ecs.components.DamageComponent;

import java.util.*;

/**
 * Système pour gérer les dégâts entre les entités dans Valtherion.
 * Applique des dégâts aux entités en fonction des collisions et des propriétés des entités.
 */
public class DamageSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Système de collision associé
    private CollisionSystem collisionSystem;

    // Système de santé associé
    private HealthSystem healthSystem;

    // Liste des entités avec un composant de dégâts
    private List<Entity> damageEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    // Liste des observateurs de dégâts
    private final List<DamageListener> listeners;

    /**
     * Constructeur du système de dégâts.
     */
    public DamageSystem() {
        this.damageEntities = new ArrayList<>();
        this.isEnabled = true;
        this.listeners = new ArrayList<>();
    }

    /**
     * Constructeur du système de dégâts avec un système de collision.
     * @param collisionSystem Système de collision associé.
     */
    public DamageSystem(CollisionSystem collisionSystem) {
        this();
        this.collisionSystem = collisionSystem;
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
        if (ecsManager != null) {
            healthSystem = ecsManager.getSystem(HealthSystem.class);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null) {
            return;
        }

        // Récupérer les entités avec un composant de dégâts
        damageEntities = ecsManager.getEntitiesWithComponent(DamageComponent.class);

        // Vérifier les collisions et appliquer les dégâts
        checkCollisionsAndApplyDamage();
    }

    /**
     * Vérifie les collisions et applique les dégâts.
     */
    private void checkCollisionsAndApplyDamage() {
        if (collisionSystem == null) {
            return;
        }

        // Récupérer les collisions détectées
        List<fr.alessdevita.valtherion.physics.Collision> collisions = collisionSystem.getCollisions();

        for (fr.alessdevita.valtherion.physics.Collision collision : collisions) {
            Entity entityA = collision.getBodyA().getEntity();
            Entity entityB = collision.getBodyB().getEntity();

            // Vérifier si l'une des entités a un composant de dégâts
            if (entityA.hasComponent((Class<? extends Component>) DamageComponent.class)) {
                applyDamage(entityA, entityB, collision);
            }

            if (entityB.hasComponent((Class<? extends Component>) DamageComponent.class)) {
                applyDamage(entityB, entityA, collision);
            }
        }
    }

    /**
     * Applique des dégâts à une entité.
     * @param damager Entité qui inflige les dégâts.
     * @param victim Entité qui reçoit les dégâts.
     * @param collision Collision associée.
     */
    private void applyDamage(Entity damager, Entity victim, fr.alessdevita.valtherion.physics.Collision collision) {
        DamageComponent damageComponent = damager.getComponent(DamageComponent.class);
        if (damageComponent == null || !damageComponent.isEnabled()) {
            return;
        }

        // Vérifier si la victime a un composant de santé
        if (victim.hasComponent(HealthComponent.class)) {
            float damage = damageComponent.getDamage();

            // Appliquer les dégâts à la victime
            if (healthSystem != null) {
                healthSystem.damageEntity(victim.getId(), damage, damager);
            }

            // Notifier les observateurs
            notifyDamageListeners(damager, victim, damage);
        }
    }

    /**
     * Inflige des dégâts à une entité.
     * @param damagerId ID de l'entité qui inflige les dégâts.
     * @param victimId ID de l'entité qui reçoit les dégâts.
     * @param damage Montant des dégâts.
     */
    public void dealDamage(String damagerId, String victimId, float damage) {
        Entity damager = ecsManager.getEntity(damagerId);
        Entity victim = ecsManager.getEntity(victimId);

        if (damager != null && victim != null && victim.hasComponent(HealthComponent.class)) {
            if (healthSystem != null) {
                healthSystem.damageEntity(victimId, damage, damager);
            }
            notifyDamageListeners(damager, victim, damage);
        }
    }

    /**
     * Notifie les observateurs des dégâts.
     * @param damager Entité qui inflige les dégâts.
     * @param victim Entité qui reçoit les dégâts.
     * @param damage Montant des dégâts.
     */
    private void notifyDamageListeners(Entity damager, Entity victim, float damage) {
        for (DamageListener listener : listeners) {
            listener.onDamage(damager, victim, damage);
        }
    }

    /**
     * Ajoute un observateur de dégâts.
     * @param listener Observateur à ajouter.
     */
    public void addDamageListener(DamageListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur de dégâts.
     * @param listener Observateur à retirer.
     */
    public void removeDamageListener(DamageListener listener) {
        listeners.remove(listener);
    }

    // --- Getters et Setters ---

    public CollisionSystem getCollisionSystem() {
        return collisionSystem;
    }

    public void setCollisionSystem(CollisionSystem collisionSystem) {
        this.collisionSystem = collisionSystem;
    }

    public HealthSystem getHealthSystem() {
        return healthSystem;
    }

    public void setHealthSystem(HealthSystem healthSystem) {
        this.healthSystem = healthSystem;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getDamageEntities() {
        return damageEntities;
    }

    // --- Interface pour les observateurs de dégâts ---
    public interface DamageListener {
        void onDamage(Entity damager, Entity victim, float damage);
    }
}