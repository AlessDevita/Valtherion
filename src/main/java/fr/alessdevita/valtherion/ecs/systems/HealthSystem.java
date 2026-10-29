package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.HealthComponent;

import java.util.*;

/**
 * Système pour gérer la santé des entités dans Valtherion.
 */
public class HealthSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Liste des entités avec un composant de santé
    private List<Entity> healthEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    /**
     * Constructeur du système de santé.
     */
    public HealthSystem() {
        this.healthEntities = new ArrayList<>();
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

        // Récupérer les entités avec le composant Health
        healthEntities = ecsManager.getEntitiesWithComponent(HealthComponent.class);

        // Mettre à jour la santé de chaque entité
        for (Entity entity : healthEntities) {
            HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
            if (healthComponent != null) {
                healthComponent.update(deltaTime);
            }
        }
    }

    /**
     * Inflige des dégâts à une entité.
     * @param entityId ID de l'entité.
     * @param damage Montant des dégâts.
     * @param damager Entité qui inflige les dégâts.
     */
    public void damageEntity(String entityId, float damage, Entity damager) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(HealthComponent.class)) {
            HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
            healthComponent.damage(damage, damager);
        }
    }

    /**
     * Soigne une entité.
     * @param entityId ID de l'entité.
     * @param amount Montant de soins.
     */
    public void healEntity(String entityId, float amount) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(HealthComponent.class)) {
            HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
            healthComponent.heal(amount);
        }
    }

    /**
     * Tue une entité.
     * @param entityId ID de l'entité.
     */
    public void killEntity(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(HealthComponent.class)) {
            HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
            healthComponent.setHealth(0);
        }
    }

    /**
     * Vérifie si une entité est en vie.
     * @param entityId ID de l'entité.
     * @return true si l'entité est en vie, false sinon.
     */
    public boolean isEntityAlive(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(HealthComponent.class)) {
            HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
            return healthComponent.isAlive();
        }
        return false;
    }

    /**
     * Récupère les points de vie d'une entité.
     * @param entityId ID de l'entité.
     * @return Points de vie de l'entité.
     */
    public float getEntityHealth(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(HealthComponent.class)) {
            HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
            return healthComponent.getHealth();
        }
        return 0.0f;
    }

    // --- Getters et Setters ---

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getHealthEntities() {
        return healthEntities;
    }
}