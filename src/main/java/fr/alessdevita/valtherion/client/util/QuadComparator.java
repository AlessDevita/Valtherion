package fr.alessdevita.valtherion.client.util;

import java.util.Comparator;

/**
 * Comparateur de quads pour le rendu translucide.
 * <p>
 * Trie les quads du plus éloigné au plus proche de la caméra
 * (back-to-front) pour un blending correct.
 */
public final class QuadComparator implements Comparator<QuadComparator.Quad> {
    private double cameraX;
    private double cameraY;
    private double cameraZ;

    /**
     * Représente un quad avec une position de référence.
     */
    public static final class Quad {
        private final double x;
        private final double y;
        private final double z;

        public Quad(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() { return this.x; }
        public double getY() { return this.y; }
        public double getZ() { return this.z; }
    }

    public QuadComparator() {
        this(0, 0, 0);
    }

    public QuadComparator(double cameraX, double cameraY, double cameraZ) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.cameraZ = cameraZ;
    }

    /** Met à jour la position de la caméra. */
    public void setCameraPosition(double x, double y, double z) {
        this.cameraX = x;
        this.cameraY = y;
        this.cameraZ = z;
    }

    @Override
    public int compare(Quad first, Quad second) {
        if (first == second) return 0;
        if (first == null) return 1;
        if (second == null) return -1;
        // Back-to-front : le plus éloigné d'abord.
        return Double.compare(distanceSquared(second), distanceSquared(first));
    }

    private double distanceSquared(Quad quad) {
        double dx = quad.x - this.cameraX;
        double dy = quad.y - this.cameraY;
        double dz = quad.z - this.cameraZ;
        return dx * dx + dy * dy + dz * dz;
    }
}
