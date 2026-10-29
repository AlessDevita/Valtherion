package fr.alessdevita.valtherion.physics;

import org.joml.Vector3f;

/**
 * Classe pour une boîte de collision alignée sur les axes (AABB).
 * Utilisée pour détecter les collisions entre les entités.
 */
public class AABB {

    // Position du centre de la boîte
    private Vector3f center;

    // Demi-dimensions de la boîte (largeur/2, hauteur/2, profondeur/2)
    private Vector3f halfExtents;

    // Position minimale de la boîte (coin inférieur gauche arrière)
    private Vector3f min;

    // Position maximale de la boîte (coin supérieur droit avant)
    private Vector3f max;

    /**
     * Constructeur de la boîte de collision.
     * @param center Position du centre de la boîte.
     * @param halfExtents Demi-dimensions de la boîte.
     */
    public AABB(Vector3f center, Vector3f halfExtents) {
        this.center = center;
        this.halfExtents = halfExtents;
        updateMinMax();
    }

    /**
     * Met à jour les positions minimale et maximale de la boîte.
     */
    private void updateMinMax() {
        this.min = new Vector3f(
                center.x - halfExtents.x,
                center.y - halfExtents.y,
                center.z - halfExtents.z
        );
        this.max = new Vector3f(
                center.x + halfExtents.x,
                center.y + halfExtents.y,
                center.z + halfExtents.z
        );
    }

    /**
     * Vérifie si cette boîte entre en collision avec une autre boîte.
     * @param other Autre boîte de collision.
     * @return true si les boîtes entrent en collision, false sinon.
     */
    public boolean intersects(AABB other) {
        return min.x <= other.max.x && max.x >= other.min.x &&
                min.y <= other.max.y && max.y >= other.min.y &&
                min.z <= other.max.z && max.z >= other.min.z;
    }

    /**
     * Vérifie si un point est à l'intérieur de la boîte.
     * @param point Point à vérifier.
     * @return true si le point est à l'intérieur de la boîte, false sinon.
     */
    public boolean contains(Vector3f point) {
        return point.x >= min.x && point.x <= max.x &&
                point.y >= min.y && point.y <= max.y &&
                point.z >= min.z && point.z <= max.z;
    }

    /**
     * Calcule l'intersection entre cette boîte et une autre.
     * @param other Autre boîte de collision.
     * @return Vecteur de poussée pour résoudre la collision (null si pas de collision).
     */
    public Vector3f getCollisionPush(AABB other) {
        if (!intersects(other)) {
            return null;
        }

        // Calculer les chevauchements sur chaque axe
        float overlapX = Math.min(max.x, other.max.x) - Math.max(min.x, other.min.x);
        float overlapY = Math.min(max.y, other.max.y) - Math.max(min.y, other.min.y);
        float overlapZ = Math.min(max.z, other.max.z) - Math.max(min.z, other.min.z);

        // Trouver l'axe avec le plus petit chevauchement
        if (overlapX < overlapY && overlapX < overlapZ) {
            // Pousser sur l'axe X
            float pushX = (center.x < other.center.x) ? -overlapX : overlapX;
            return new Vector3f(pushX, 0, 0);
        } else if (overlapY < overlapZ) {
            // Pousser sur l'axe Y
            float pushY = (center.y < other.center.y) ? -overlapY : overlapY;
            return new Vector3f(0, pushY, 0);
        } else {
            // Pousser sur l'axe Z
            float pushZ = (center.z < other.center.z) ? -overlapZ : overlapZ;
            return new Vector3f(0, 0, pushZ);
        }
    }

    /**
     * Calcule la distance entre cette boîte et un point.
     * @param point Point à vérifier.
     * @return Distance entre la boîte et le point.
     */
    public float distanceTo(Vector3f point) {
        float dx = Math.max(Math.abs(point.x - center.x) - halfExtents.x, 0);
        float dy = Math.max(Math.abs(point.y - center.y) - halfExtents.y, 0);
        float dz = Math.max(Math.abs(point.z - center.z) - halfExtents.z, 0);
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    // --- Getters et Setters ---

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
        updateMinMax();
    }

    public Vector3f getHalfExtents() {
        return halfExtents;
    }

    public void setHalfExtents(Vector3f halfExtents) {
        this.halfExtents = halfExtents;
        updateMinMax();
    }

    public Vector3f getMin() {
        return min;
    }

    public Vector3f getMax() {
        return max;
    }

    /**
     * Retourne une représentation textuelle de la boîte de collision.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "AABB{" +
                "center=" + center +
                ", halfExtents=" + halfExtents +
                '}';
    }

    public Vector3f getClosestPoint(Vector3f position) {
        return null;
    }
}