package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;
import fr.alessdevita.valtherion.ecs.components.PhysicsComponent;
import fr.alessdevita.valtherion.ecs.components.CollisionComponent;
import fr.alessdevita.valtherion.physics.PhysicsEngine;
import fr.alessdevita.valtherion.physics.Collision;
import fr.alessdevita.valtherion.physics.Collider;

import org.joml.Vector3f;

import java.util.*;

/**
 * Système pour gérer les collisions entre les entités dans Valtherion.
 * Détecte et résout les collisions entre les entités avec des composants de physique.
 */
public class CollisionSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Moteur physique associé
    private PhysicsEngine physicsEngine;

    // Liste des entités avec un composant de physique
    private List<Entity> physicsEntities;

    // Liste des collisions détectées
    private final List<Collision> collisions;

    // Indique si le système est activé
    private boolean isEnabled;

    // Liste des observateurs de collision
    private final List<CollisionListener> listeners;

    /**
     * Constructeur du système de collision.
     * @param physicsEngine Moteur physique associé.
     */
    public CollisionSystem(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
        this.physicsEntities = new ArrayList<>();
        this.collisions = new ArrayList<>();
        this.isEnabled = true;
        this.listeners = new ArrayList<>();
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

        // Récupérer les collisions détectées par le moteur physique
        collisions.clear();
        collisions.addAll(physicsEngine.getCollisions());

        // Résoudre les collisions
        resolveCollisions();

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Résout les collisions détectées.
     */
    private void resolveCollisions() {
        for (Collision collision : collisions) {
            // Résoudre la collision
            collision.resolve();

            // Notifier les observateurs pour cette collision
            notifyCollisionListeners(collision);
        }
    }

    /**
     * Notifie les observateurs des collisions.
     */
    private void notifyListeners() {
        for (CollisionListener listener : listeners) {
            listener.onCollisionUpdate(collisions);
        }
    }

    /**
     * Notifie les observateurs pour une collision spécifique.
     * @param collision Collision à notifier.
     */
    private void notifyCollisionListeners(Collision collision) {
        for (CollisionListener listener : listeners) {
            listener.onCollision(collision);
        }
    }

    /**
     * Vérifie si une entité est en collision avec une autre.
     * @param entityId1 ID de la première entité.
     * @param entityId2 ID de la deuxième entité.
     * @return true si les entités sont en collision, false sinon.
     */
    public boolean isColliding(String entityId1, String entityId2) {
        Entity entity1 = ecsManager.getEntity(entityId1);
        Entity entity2 = ecsManager.getEntity(entityId2);

        if (entity1 == null || entity2 == null) {
            return false;
        }

        PhysicsComponent physics1 = entity1.getComponent(PhysicsComponent.class);
        PhysicsComponent physics2 = entity2.getComponent(PhysicsComponent.class);

        if (physics1 == null || physics2 == null) {
            return false;
        }

        Collider collider1 = physics1.getCollider();
        Collider collider2 = physics2.getCollider();

        return collider1.intersects(collider2);
    }

    /**
     * Vérifie si une entité est en collision avec une position.
     * @param entityId ID de l'entité.
     * @param position Position à vérifier.
     * @return true si l'entité est en collision avec la position, false sinon.
     */
    public boolean isCollidingWithPosition(String entityId, Vector3f position) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity == null) {
            return false;
        }

        PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);
        if (physics == null) {
            return false;
        }

        Collider collider = physics.getCollider();
        return collider.contains(position);
    }

    /**
     * Ajoute un observateur de collision.
     * @param listener Observateur à ajouter.
     */
    public void addCollisionListener(CollisionListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur de collision.
     * @param listener Observateur à retirer.
     */
    public void removeCollisionListener(CollisionListener listener) {
        listeners.remove(listener);
    }

    // --- Getters et Setters ---

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    public List<Entity> getPhysicsEntities() {
        return physicsEntities;
    }

    public List<Collision> getCollisions() {
        return collisions;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    // --- Interface pour les observateurs de collision ---
    public interface CollisionListener {
        void onCollisionUpdate(List<Collision> collisions);
        void onCollision(Collision collision);
    }
}