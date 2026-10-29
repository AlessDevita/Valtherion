package fr.alessdevita.valtherion.physics;

import org.joml.Vector3f;

/**
 * Classe pour représenter une collision entre deux corps physiques dans Valtherion.
 * Fournit des informations sur les corps impliqués, le point de contact, et la normale de collision.
 */
public class Collision {

    // Corps physiques impliqués dans la collision
    private final PhysicsBody bodyA;
    private final PhysicsBody bodyB;

    // Point de contact entre les deux corps
    private Vector3f contactPoint;

    // Normale de la collision (direction de la poussée)
    private Vector3f contactNormal;

    // Profondeur de la pénétration (distance de chevauchement)
    private float penetrationDepth;

    // Indique si la collision a déjà été résolue
    private boolean isResolved;

    // Temps de la collision (pour les simulations temporelles)
    private float collisionTime;

    /**
     * Constructeur de la collision.
     * @param bodyA Premier corps physique.
     * @param bodyB Deuxième corps physique.
     */
    public Collision(PhysicsBody bodyA, PhysicsBody bodyB) {
        this.bodyA = bodyA;
        this.bodyB = bodyB;
        this.contactPoint = calculateContactPoint();
        this.contactNormal = calculateContactNormal();
        this.penetrationDepth = calculatePenetrationDepth();
        this.isResolved = false;
        this.collisionTime = 0.0f;
    }

    /**
     * Calcule le point de contact entre les deux corps.
     * @return Point de contact.
     */
    private Vector3f calculateContactPoint() {
        Collider colliderA = bodyA.getCollider();
        Collider colliderB = bodyB.getCollider();

        // Si les deux collisionneurs sont des boîtes (AABB)
        if (colliderA.getType() == Collider.ColliderType.AABB &&
                colliderB.getType() == Collider.ColliderType.AABB) {
            BoxCollider boxA = (BoxCollider) colliderA;
            BoxCollider boxB = (BoxCollider) colliderB;

            // Calculer le point de contact entre deux boîtes
            Vector3f minA = boxA.getAABB().getMin();
            Vector3f maxA = boxA.getAABB().getMax();
            Vector3f minB = boxB.getAABB().getMin();
            Vector3f maxB = boxB.getAABB().getMax();

            // Trouver le point le plus proche entre les deux boîtes
            float closestX = Math.max(minA.x, minB.x);
            float closestY = Math.max(minA.y, minB.y);
            float closestZ = Math.max(minA.z, minB.z);

            closestX = Math.min(closestX, Math.min(maxA.x, maxB.x));
            closestY = Math.min(closestY, Math.min(maxA.y, maxB.y));
            closestZ = Math.min(closestZ, Math.min(maxA.z, maxB.z));

            return new Vector3f(closestX, closestY, closestZ);
        }

        // Si l'un des collisionneurs est une sphère
        if (colliderA.getType() == Collider.ColliderType.SPHERE) {
            SphereCollider sphereA = (SphereCollider) colliderA;
            return sphereA.getClosestPoint(colliderB.getPosition());
        } else if (colliderB.getType() == Collider.ColliderType.SPHERE) {
            SphereCollider sphereB = (SphereCollider) colliderB;
            return sphereB.getClosestPoint(colliderA.getPosition());
        }

        // Par défaut, retourner le point milieu entre les deux centres
        Vector3f centerA = colliderA.getPosition();
        Vector3f centerB = colliderB.getPosition();
        return new Vector3f(
                (centerA.x + centerB.x) / 2.0f,
                (centerA.y + centerB.y) / 2.0f,
                (centerA.z + centerB.z) / 2.0f
        );
    }

    /**
     * Calcule la normale de la collision.
     * @return Normale de la collision.
     */
    private Vector3f calculateContactNormal() {
        Collider colliderA = bodyA.getCollider();
        Collider colliderB = bodyB.getCollider();

        // Si les deux collisionneurs sont des boîtes (AABB)
        if (colliderA.getType() == Collider.ColliderType.AABB &&
                colliderB.getType() == Collider.ColliderType.AABB) {
            BoxCollider boxA = (BoxCollider) colliderA;
            BoxCollider boxB = (BoxCollider) colliderB;

            // Calculer la normale entre les deux boîtes
            Vector3f centerA = boxA.getAABB().getCenter();
            Vector3f centerB = boxB.getAABB().getCenter();
            return new Vector3f(
                    centerB.x - centerA.x,
                    centerB.y - centerA.y,
                    centerB.z - centerA.z
            ).normalize();
        }

        // Si l'un des collisionneurs est une sphère
        if (colliderA.getType() == Collider.ColliderType.SPHERE) {
            SphereCollider sphereA = (SphereCollider) colliderA;
            Vector3f closestPoint = sphereA.getClosestPoint(colliderB.getPosition());
            return new Vector3f(
                    closestPoint.x - sphereA.getPosition().x,
                    closestPoint.y - sphereA.getPosition().y,
                    closestPoint.z - sphereA.getPosition().z
            ).normalize();
        } else if (colliderB.getType() == Collider.ColliderType.SPHERE) {
            SphereCollider sphereB = (SphereCollider) colliderB;
            Vector3f closestPoint = sphereB.getClosestPoint(colliderA.getPosition());
            return new Vector3f(
                    closestPoint.x - sphereB.getPosition().x,
                    closestPoint.y - sphereB.getPosition().y,
                    closestPoint.z - sphereB.getPosition().z
            ).normalize();
        }

        // Par défaut, retourner la normale entre les deux centres
        Vector3f centerA = colliderA.getPosition();
        Vector3f centerB = colliderB.getPosition();
        return new Vector3f(
                centerB.x - centerA.x,
                centerB.y - centerA.y,
                centerB.z - centerA.z
        ).normalize();
    }

    /**
     * Calcule la profondeur de pénétration entre les deux corps.
     * @return Profondeur de pénétration.
     */
    private float calculatePenetrationDepth() {
        Collider colliderA = bodyA.getCollider();
        Collider colliderB = bodyB.getCollider();

        // Si les deux collisionneurs sont des boîtes (AABB)
        if (colliderA.getType() == Collider.ColliderType.AABB &&
                colliderB.getType() == Collider.ColliderType.AABB) {
            BoxCollider boxA = (BoxCollider) colliderA;
            BoxCollider boxB = (BoxCollider) colliderB;

            // Calculer la profondeur de pénétration entre deux boîtes
            Vector3f minA = boxA.getAABB().getMin();
            Vector3f maxA = boxA.getAABB().getMax();
            Vector3f minB = boxB.getAABB().getMin();
            Vector3f maxB = boxB.getAABB().getMax();

            float overlapX = Math.min(maxA.x, maxB.x) - Math.max(minA.x, minB.x);
            float overlapY = Math.min(maxA.y, maxB.y) - Math.max(minA.y, minB.y);
            float overlapZ = Math.min(maxA.z, maxB.z) - Math.max(minA.z, minB.z);

            return Math.min(Math.min(overlapX, overlapY), overlapZ);
        }

        // Si l'un des collisionneurs est une sphère
        if (colliderA.getType() == Collider.ColliderType.SPHERE) {
            SphereCollider sphereA = (SphereCollider) colliderA;
            float distance = sphereA.distanceTo(colliderB.getPosition());
            return sphereA.getRadius() - distance;
        } else if (colliderB.getType() == Collider.ColliderType.SPHERE) {
            SphereCollider sphereB = (SphereCollider) colliderB;
            float distance = sphereB.distanceTo(colliderA.getPosition());
            return sphereB.getRadius() - distance;
        }

        // Par défaut, retourner 0
        return 0.0f;
    }

    /**
     * Résout la collision en appliquant une impulsion aux corps.
     */
    public void resolve() {
        if (isResolved) {
            return;
        }

        // Calculer la vélocité relative
        Vector3f relativeVelocity = new Vector3f(
                bodyB.getVelocity().x - bodyA.getVelocity().x,
                bodyB.getVelocity().y - bodyA.getVelocity().y,
                bodyB.getVelocity().z - bodyA.getVelocity().z
        );

        // Calculer la vélocité relative le long de la normale
        float velocityAlongNormal = relativeVelocity.dot(contactNormal);

        // Ne pas résoudre la collision si les corps s'éloignent
        if (velocityAlongNormal > 0) {
            isResolved = true;
            return;
        }

        // Calculer la masse totale
        float totalMass = bodyA.getMass() + bodyB.getMass();

        // Calculer le coefficient de restitution (rebond)
        float restitution = Math.min(bodyA.getRestitution(), bodyB.getRestitution());

        // Calculer l'impulsion
        float impulseMagnitude = -(1 + restitution) * velocityAlongNormal / totalMass;
        Vector3f impulse = new Vector3f(contactNormal).mul(impulseMagnitude);

        // Appliquer l'impulsion aux corps
        if (!bodyA.isStatic()) {
            bodyA.applyImpulse(new Vector3f(impulse).mul(bodyB.getMass()));
        }

        if (!bodyB.isStatic()) {
            bodyB.applyImpulse(new Vector3f(impulse).mul(-bodyA.getMass()));
        }

        // Résoudre la pénétration
        resolvePenetration();

        isResolved = true;
    }

    /**
     * Résout la pénétration entre les deux corps.
     */
    private void resolvePenetration() {
        if (penetrationDepth <= 0) {
            return;
        }

        // Calculer la masse totale
        float totalMass = bodyA.getMass() + bodyB.getMass();

        // Calculer le ratio de masse
        float massRatioA = bodyA.getMass() / totalMass;
        float massRatioB = bodyB.getMass() / totalMass;

        // Calculer le vecteur de résolution
        Vector3f resolutionVector = new Vector3f(contactNormal).mul(penetrationDepth);

        // Appliquer la résolution aux corps
        if (!bodyA.isStatic()) {
            bodyA.getEntity().move(
                    resolutionVector.x * massRatioB,
                    resolutionVector.y * massRatioB,
                    resolutionVector.z * massRatioB
            );
        }

        if (!bodyB.isStatic()) {
            bodyB.getEntity().move(
                    -resolutionVector.x * massRatioA,
                    -resolutionVector.y * massRatioA,
                    -resolutionVector.z * massRatioA
            );
        }
    }

    // --- Getters et Setters ---

    public PhysicsBody getBodyA() {
        return bodyA;
    }

    public PhysicsBody getBodyB() {
        return bodyB;
    }

    public Vector3f getContactPoint() {
        return contactPoint;
    }

    public void setContactPoint(Vector3f contactPoint) {
        this.contactPoint = contactPoint;
    }

    public Vector3f getContactNormal() {
        return contactNormal;
    }

    public void setContactNormal(Vector3f contactNormal) {
        this.contactNormal = contactNormal;
    }

    public float getPenetrationDepth() {
        return penetrationDepth;
    }

    public void setPenetrationDepth(float penetrationDepth) {
        this.penetrationDepth = penetrationDepth;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean isResolved) {
        this.isResolved = isResolved;
    }

    public float getCollisionTime() {
        return collisionTime;
    }

    public void setCollisionTime(float collisionTime) {
        this.collisionTime = collisionTime;
    }

    /**
     * Retourne une représentation textuelle de la collision.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Collision{" +
                "bodyA=" + (bodyA != null ? bodyA.getEntity().getName() : "null") +
                ", bodyB=" + (bodyB != null ? bodyB.getEntity().getName() : "null") +
                ", contactPoint=" + contactPoint +
                ", contactNormal=" + contactNormal +
                ", penetrationDepth=" + penetrationDepth +
                '}';
    }
}