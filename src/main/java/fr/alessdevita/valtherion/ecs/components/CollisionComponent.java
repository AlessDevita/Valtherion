package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.physics.Collider;
import fr.alessdevita.valtherion.physics.BoxCollider;
import fr.alessdevita.valtherion.physics.SphereCollider;

import org.joml.Vector3f;

import java.util.*;

/**
 * Composant pour les collisions d'une entité dans Valtherion.
 * Stocke les informations sur les collisions et les propriétés de collision.
 */
public class CollisionComponent implements Component {

    // Collisionneur de l'entité
    private Collider collider;

    // Type de collisionneur
    private ColliderType colliderType;

    // Taille du collisionneur (pour les boîtes et les sphères)
    private Vector3f size;

    // Rayon du collisionneur (pour les sphères)
    private float radius;

    // Indique si le collisionneur est un déclencheur (ne bloque pas les mouvements)
    private boolean isTrigger;

    // Liste des entités en collision avec cette entité
    private final List<Entity> collidingEntities;

    // Liste des tags des entités avec lesquelles cette entité peut entrer en collision
    private final List<String> collisionTags;

    // Indique si les collisions sont activées
    private boolean isCollisionEnabled;

    // Entité associée
    private Entity entity;

    // Énumération des types de collisionneurs
    public enum ColliderType {
        BOX,       // Boîte (AABB)
        SPHERE,    // Sphère
        CAPSULE,   // Capsule
        MESH       // Maillage 3D
    }

    /**
     * Constructeur du composant de collision.
     */
    public CollisionComponent() {
        this.collider = null;
        this.colliderType = ColliderType.BOX;
        this.size = new Vector3f(1.0f, 1.0f, 1.0f);
        this.radius = 0.5f;
        this.isTrigger = false;
        this.collidingEntities = new ArrayList<>();
        this.collisionTags = new ArrayList<>();
        this.isCollisionEnabled = true;
    }

    /**
     * Constructeur du composant de collision avec un type et une taille.
     * @param colliderType Type de collisionneur.
     * @param size Taille du collisionneur.
     */
    public CollisionComponent(ColliderType colliderType, Vector3f size) {
        this();
        this.colliderType = colliderType;
        this.size = size;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        updateCollider();
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void init() {
        updateCollider();
    }

    /**
     * Met à jour le collisionneur en fonction du type et de la taille.
     */
    private void updateCollider() {
        if (entity == null || !entity.hasComponent(TransformComponent.class)) {
            return;
        }

        TransformComponent transform = entity.getComponent(TransformComponent.class);
        Vector3f position = transform.getPosition();

        switch (colliderType) {
            case BOX:
                collider = new BoxCollider(position, new Vector3f(size.x / 2, size.y / 2, size.z / 2));
                break;
            case SPHERE:
                collider = new SphereCollider(position, radius);
                break;
            case CAPSULE:
                // À implémenter selon tes besoins
                break;
            case MESH:
                // À implémenter selon tes besoins
                break;
        }

        if (collider != null) {
            collider.setEntity(entity);
        }
    }

    /**
     * Vérifie si cette entité entre en collision avec une autre entité.
     * @param otherEntity Autre entité.
     * @return true si les entités entrent en collision, false sinon.
     */
    public boolean isColliding(Entity otherEntity) {
        if (!isCollisionEnabled || collider == null || otherEntity == null) {
            return false;
        }

        CollisionComponent otherCollision = otherEntity.getComponent(CollisionComponent.class);
        if (otherCollision == null || otherCollision.collider == null) {
            return false;
        }

        return collider.intersects(otherCollision.collider);
    }

    /**
     * Vérifie si cette entité entre en collision avec une position.
     * @param position Position à vérifier.
     * @return true si l'entité entre en collision avec la position, false sinon.
     */
    public boolean isColliding(Vector3f position) {
        return collider != null && collider.contains(position);
    }

    /**
     * Ajoute une entité en collision.
     * @param entity Entité en collision.
     */
    public void addCollidingEntity(Entity entity) {
        if (entity != null && !collidingEntities.contains(entity)) {
            collidingEntities.add(entity);
        }
    }

    /**
     * Retire une entité en collision.
     * @param entity Entité à retirer.
     */
    public void removeCollidingEntity(Entity entity) {
        collidingEntities.remove(entity);
    }

    /**
     * Vérifie si une entité est en collision avec cette entité.
     * @param entity Entité à vérifier.
     * @return true si l'entité est en collision, false sinon.
     */
    public boolean hasCollidingEntity(Entity entity) {
        return collidingEntities.contains(entity);
    }

    /**
     * Ajoute un tag de collision.
     * @param tag Tag à ajouter.
     */
    public void addCollisionTag(String tag) {
        if (tag != null && !collisionTags.contains(tag)) {
            collisionTags.add(tag);
        }
    }

    /**
     * Vérifie si ce composant peut entrer en collision avec un tag.
     * @param tag Tag à vérifier.
     * @return true si le tag est dans la liste des tags de collision, false sinon.
     */
    public boolean canCollideWithTag(String tag) {
        return collisionTags.contains(tag);
    }

    // --- Getters et Setters ---

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
    }

    public ColliderType getColliderType() {
        return colliderType;
    }

    public void setColliderType(ColliderType colliderType) {
        this.colliderType = colliderType;
        updateCollider();
    }

    public Vector3f getSize() {
        return size;
    }

    public void setSize(Vector3f size) {
        this.size = size;
        updateCollider();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        updateCollider();
    }

    public boolean isTrigger() {
        return isTrigger;
    }

    public void setTrigger(boolean isTrigger) {
        this.isTrigger = isTrigger;
    }

    public List<Entity> getCollidingEntities() {
        return collidingEntities;
    }

    public List<String> getCollisionTags() {
        return collisionTags;
    }

    public boolean isCollisionEnabled() {
        return isCollisionEnabled;
    }

    public void setCollisionEnabled(boolean isCollisionEnabled) {
        this.isCollisionEnabled = isCollisionEnabled;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "CollisionComponent{" +
                "colliderType=" + colliderType +
                ", size=" + size +
                ", radius=" + radius +
                ", isTrigger=" + isTrigger +
                ", isCollisionEnabled=" + isCollisionEnabled +
                '}';
    }
}