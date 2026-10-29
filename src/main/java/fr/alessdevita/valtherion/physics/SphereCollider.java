package fr.alessdevita.valtherion.physics;

import org.joml.Vector3f;

/**
 * Classe pour un collisionneur de type sphère.
 */
public class SphereCollider extends Collider {

    // Rayon de la sphère
    private float radius;

    /**
     * Constructeur du collisionneur de type sphère.
     * @param position Position du centre de la sphère.
     * @param radius Rayon de la sphère.
     */
    public SphereCollider(Vector3f position, float radius) {
        super(position, ColliderType.SPHERE);
        this.radius = radius;
    }

    @Override
    public boolean intersects(Collider other) {
        if (other.getType() == ColliderType.SPHERE) {
            SphereCollider sphereOther = (SphereCollider) other;
            float distance = distanceTo(sphereOther.position);
            return distance <= (radius + sphereOther.radius);
        } else if (other.getType() == ColliderType.AABB) {
            BoxCollider boxOther = (BoxCollider) other;
            return boxOther.getAABB().distanceTo(position) <= radius;
        }
        // À implémenter pour d'autres types de collisionneurs
        return false;
    }

    @Override
    public float distanceTo(Vector3f point) {
        float dx = point.x - position.x;
        float dy = point.y - position.y;
        float dz = point.z - position.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz) - radius;
    }

    @Override
    public Vector3f getCollisionPush(Collider other) {
        if (other.getType() == ColliderType.SPHERE) {
            SphereCollider sphereOther = (SphereCollider) other;
            float distance = distanceTo(sphereOther.position);
            if (distance <= (radius + sphereOther.radius)) {
                float pushDistance = (radius + sphereOther.radius) - distance;
                Vector3f direction = new Vector3f(
                        sphereOther.position.x - position.x,
                        sphereOther.position.y - position.y,
                        sphereOther.position.z - position.z
                ).normalize();
                return new Vector3f(direction).mul(pushDistance);
            }
        } else if (other.getType() == ColliderType.AABB) {
            BoxCollider boxOther = (BoxCollider) other;
            if (boxOther.getAABB().distanceTo(position) <= radius) {
                // Calculer le vecteur de poussée pour une collision entre une sphère et une boîte
                Vector3f closestPoint = boxOther.getAABB().getClosestPoint(position);
                Vector3f direction = new Vector3f(
                        position.x - closestPoint.x,
                        position.y - closestPoint.y,
                        position.z - closestPoint.z
                ).normalize();
                float pushDistance = radius - new Vector3f(position).distance(closestPoint);
                return new Vector3f(direction).mul(pushDistance);
            }
        }
        // À implémenter pour d'autres types de collisionneurs
        return null;
    }

    @Override
    public boolean contains(Vector3f point) {
        float distance = distanceTo(point);
        return distance <= 0;
    }

    /**
     * Récupère le point le plus proche de la sphère.
     * @param point Point à vérifier.
     * @return Point le plus proche de la sphère.
     */
    public Vector3f getClosestPoint(Vector3f point) {
        Vector3f direction = new Vector3f(
                point.x - position.x,
                point.y - position.y,
                point.z - position.z
        ).normalize();
        return new Vector3f(
                position.x + direction.x * radius,
                position.y + direction.y * radius,
                position.z + direction.z * radius
        );
    }

    // --- Getters et Setters ---

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}