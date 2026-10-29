package fr.alessdevita.valtherion.physics;

import fr.alessdevita.valtherion.ecs.Entity;
import org.joml.Vector3f;

/**
 * Classe de base pour un collisionneur dans Valtherion.
 * Représente une forme géométrique utilisée pour détecter les collisions.
 */
public abstract class Collider {

    public void setEntity(Entity entity) {
    }

    // Type du collisionneur
    public enum ColliderType {
        AABB,       // Boîte alignée sur les axes
        SPHERE,     // Sphère
        CAPSULE,    // Capsule
        MESH,       // Maillage 3D
        PLANE       // Plan
    }

    // Position du collisionneur
    protected Vector3f position;

    // Type du collisionneur
    protected ColliderType type;

    // Corps physique associé
    protected PhysicsBody body;

    /**
     * Constructeur du collisionneur.
     * @param position Position du collisionneur.
     * @param type Type du collisionneur.
     */
    public Collider(Vector3f position, ColliderType type) {
        this.position = position;
        this.type = type;
        this.body = null;
    }

    /**
     * Vérifie si ce collisionneur entre en collision avec un autre.
     * @param other Autre collisionneur.
     * @return true si les collisionneurs entrent en collision, false sinon.
     */
    public abstract boolean intersects(Collider other);

    /**
     * Calcule la distance entre ce collisionneur et un point.
     * @param point Point à vérifier.
     * @return Distance entre le collisionneur et le point.
     */
    public abstract float distanceTo(Vector3f point);

    /**
     * Calcule le vecteur de poussée pour résoudre une collision.
     * @param other Autre collisionneur.
     * @return Vecteur de poussée pour résoudre la collision (null si pas de collision).
     */
    public abstract Vector3f getCollisionPush(Collider other);

    /**
     * Vérifie si un point est à l'intérieur du collisionneur.
     * @param point Point à vérifier.
     * @return true si le point est à l'intérieur du collisionneur, false sinon.
     */
    public abstract boolean contains(Vector3f point);

    // --- Getters et Setters ---

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public ColliderType getType() {
        return type;
    }

    public PhysicsBody getBody() {
        return body;
    }

    public void setBody(PhysicsBody body) {
        this.body = body;
    }
}