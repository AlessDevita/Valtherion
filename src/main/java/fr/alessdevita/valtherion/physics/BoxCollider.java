package fr.alessdevita.valtherion.physics;

import org.joml.Vector3f;

/**
 * Classe pour un collisionneur de type boîte (AABB).
 */
public class BoxCollider extends Collider {

    // Demi-dimensions de la boîte
    private Vector3f halfExtents;

    // Boîte de collision associée
    private AABB aabb;

    /**
     * Constructeur du collisionneur de type boîte.
     * @param position Position du centre de la boîte.
     * @param halfExtents Demi-dimensions de la boîte.
     */
    public BoxCollider(Vector3f position, Vector3f halfExtents) {
        super(position, ColliderType.AABB);
        this.halfExtents = halfExtents;
        this.aabb = new AABB(position, halfExtents);
    }

    /**
     * Met à jour la boîte de collision.
     */
    private void updateAABB() {
        aabb.setCenter(position);
        aabb.setHalfExtents(halfExtents);
    }

    @Override
    public boolean intersects(Collider other) {
        if (other.getType() == ColliderType.AABB) {
            BoxCollider boxOther = (BoxCollider) other;
            return aabb.intersects(boxOther.aabb);
        }
        // À implémenter pour d'autres types de collisionneurs
        return false;
    }

    @Override
    public float distanceTo(Vector3f point) {
        return aabb.distanceTo(point);
    }

    @Override
    public Vector3f getCollisionPush(Collider other) {
        if (other.getType() == ColliderType.AABB) {
            BoxCollider boxOther = (BoxCollider) other;
            return aabb.getCollisionPush(boxOther.aabb);
        }
        // À implémenter pour d'autres types de collisionneurs
        return null;
    }

    @Override
    public boolean contains(Vector3f point) {
        return aabb.contains(point);
    }

    // --- Getters et Setters ---

    public Vector3f getHalfExtents() {
        return halfExtents;
    }

    public void setHalfExtents(Vector3f halfExtents) {
        this.halfExtents = halfExtents;
        updateAABB();
    }

    public AABB getAABB() {
        return aabb;
    }
}