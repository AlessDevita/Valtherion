package fr.alessdevita.valtherion.physics;

import org.joml.Vector3f;
import java.util.*;

/**
 * Classe pour la détection de raycast dans Valtherion.
 * Permet de détecter les collisions le long d'un rayon (pour les tirs, la vision, etc.).
 */
public class Raycast {

    // Point de départ du rayon
    private Vector3f origin;

    // Direction du rayon (doit être normalisé)
    private Vector3f direction;

    // Longueur maximale du rayon
    private float maxDistance;

    // Liste des corps physiques à ignorer
    private final List<PhysicsBody> ignoredBodies;

    // Résultat du raycast
    private RaycastHit hit;

    /**
     * Classe pour représenter un impact de raycast.
     */
    public static class RaycastHit {
        private PhysicsBody body;  // Corps physique touché
        private Vector3f point;    // Point d'impact
        private Vector3f normal;   // Normale au point d'impact
        private float distance;    // Distance depuis l'origine

        public RaycastHit(PhysicsBody body, Vector3f point, Vector3f normal, float distance) {
            this.body = body;
            this.point = point;
            this.normal = normal;
            this.distance = distance;
        }

        // --- Getters ---
        public PhysicsBody getBody() {
            return body;
        }

        public Vector3f getPoint() {
            return point;
        }

        public Vector3f getNormal() {
            return normal;
        }

        public float getDistance() {
            return distance;
        }
    }

    /**
     * Constructeur du raycast.
     * @param origin Point de départ du rayon.
     * @param direction Direction du rayon.
     * @param maxDistance Longueur maximale du rayon.
     */
    public Raycast(Vector3f origin, Vector3f direction, float maxDistance) {
        this.origin = origin;
        this.direction = new Vector3f(direction).normalize();
        this.maxDistance = maxDistance;
        this.ignoredBodies = new ArrayList<>();
        this.hit = null;
    }

    /**
     * Effectue le raycast.
     * @param bodies Liste des corps physiques à tester.
     * @return true si un impact a été détecté, false sinon.
     */
    public boolean cast(List<PhysicsBody> bodies) {
        hit = null;
        float closestDistance = Float.MAX_VALUE;

        for (PhysicsBody body : bodies) {
            if (ignoredBodies.contains(body)) {
                continue;
            }

            Collider collider = body.getCollider();
            RaycastHit currentHit = raycastCollider(collider);

            if (currentHit != null && currentHit.getDistance() < closestDistance) {
                hit = currentHit;
                closestDistance = currentHit.getDistance();
            }
        }

        return hit != null;
    }

    /**
     * Effectue le raycast sur un collisionneur spécifique.
     * @param collider Collisionneur à tester.
     * @return Résultat du raycast ou null si pas d'impact.
     */
    private RaycastHit raycastCollider(Collider collider) {
        if (collider.getType() == Collider.ColliderType.AABB) {
            return raycastAABB((BoxCollider) collider);
        } else if (collider.getType() == Collider.ColliderType.SPHERE) {
            return raycastSphere((SphereCollider) collider);
        }
        return null;
    }

    /**
     * Effectue le raycast sur une boîte (AABB).
     * @param boxCollider Collisionneur de type boîte.
     * @return Résultat du raycast ou null si pas d'impact.
     */
    private RaycastHit raycastAABB(BoxCollider boxCollider) {
        AABB aabb = boxCollider.getAABB();
        Vector3f min = aabb.getMin();
        Vector3f max = aabb.getMax();

        float tMin = 0.0f;
        float tMax = Float.MAX_VALUE;

        // Calculer les intersections sur chaque axe
        for (int i = 0; i < 3; i++) {
            float invD = 1.0f / direction.get(i);
            float t1 = (min.get(i) - origin.get(i)) * invD;
            float t2 = (max.get(i) - origin.get(i)) * invD;

            float tNear = Math.min(t1, t2);
            float tFar = Math.max(t1, t2);

            tMin = Math.max(tMin, tNear);
            tMax = Math.min(tMax, tFar);

            if (tMin > tMax) {
                return null; // Pas d'intersection
            }
        }

        if (tMin > maxDistance) {
            return null; // Trop loin
        }

        // Calculer le point d'impact
        Vector3f hitPoint = new Vector3f(
                origin.x + direction.x * tMin,
                origin.y + direction.y * tMin,
                origin.z + direction.z * tMin
        );

        // Calculer la normale au point d'impact
        Vector3f normal = new Vector3f(0, 0, 0);

        // Déterminer la face touchée
        float[] distances = {
                Math.abs(hitPoint.x - min.x),
                Math.abs(hitPoint.x - max.x),
                Math.abs(hitPoint.y - min.y),
                Math.abs(hitPoint.y - max.y),
                Math.abs(hitPoint.z - min.z),
                Math.abs(hitPoint.z - max.z)
        };

        float minDistance = Float.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] < minDistance) {
                minDistance = distances[i];
                minIndex = i;
            }
        }

        // Définir la normale en fonction de la face touchée
        switch (minIndex) {
            case 0: normal.set(-1, 0, 0); break; // Face gauche
            case 1: normal.set(1, 0, 0); break;  // Face droite
            case 2: normal.set(0, -1, 0); break; // Face inférieure
            case 3: normal.set(0, 1, 0); break;  // Face supérieure
            case 4: normal.set(0, 0, -1); break; // Face arrière
            case 5: normal.set(0, 0, 1); break;  // Face avant
        }

        return new RaycastHit(boxCollider.getBody(), hitPoint, normal, tMin);
    }

    /**
     * Effectue le raycast sur une sphère.
     * @param sphereCollider Collisionneur de type sphère.
     * @return Résultat du raycast ou null si pas d'impact.
     */
    private RaycastHit raycastSphere(SphereCollider sphereCollider) {
        Vector3f sphereCenter = sphereCollider.getPosition();
        float radius = sphereCollider.getRadius();

        // Calculer le vecteur entre l'origine et le centre de la sphère
        Vector3f oc = new Vector3f(
                origin.x - sphereCenter.x,
                origin.y - sphereCenter.y,
                origin.z - sphereCenter.z
        );

        float a = direction.dot(direction);
        float b = 2.0f * oc.dot(direction);
        float c = oc.dot(oc) - radius * radius;

        float discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null; // Pas d'intersection
        }

        float t = (-b - (float) Math.sqrt(discriminant)) / (2.0f * a);
        if (t > maxDistance || t < 0) {
            return null; // Trop loin ou derrière l'origine
        }

        // Calculer le point d'impact
        Vector3f hitPoint = new Vector3f(
                origin.x + direction.x * t,
                origin.y + direction.y * t,
                origin.z + direction.z * t
        );

        // Calculer la normale au point d'impact
        Vector3f normal = new Vector3f(
                hitPoint.x - sphereCenter.x,
                hitPoint.y - sphereCenter.y,
                hitPoint.z - sphereCenter.z
        ).normalize();

        return new RaycastHit(sphereCollider.getBody(), hitPoint, normal, t);
    }

    // --- Getters et Setters ---

    public Vector3f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = new Vector3f(direction).normalize();
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public RaycastHit getHit() {
        return hit;
    }

    public void addIgnoredBody(PhysicsBody body) {
        ignoredBodies.add(body);
    }

    public void removeIgnoredBody(PhysicsBody body) {
        ignoredBodies.remove(body);
    }

    public void clearIgnoredBodies() {
        ignoredBodies.clear();
    }
}