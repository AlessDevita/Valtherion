package fr.alessdevita.valtherion.client.util;

import fr.alessdevita.valtherion.entity.Entity;
import org.joml.Vector3f;

import java.util.Comparator;

/**
 * Comparateur d'entités par distance à la caméra (back-to-front).
 * <p>
 * Utilisé pour trier les entités translucides avant le rendu.
 */
public final class RenderDistanceSorter implements Comparator<Entity> {
    private Vector3f cameraPosition;

    public RenderDistanceSorter() {
        this(new Vector3f());
    }

    public RenderDistanceSorter(Vector3f cameraPosition) {
        this.cameraPosition = cameraPosition == null ? new Vector3f() : new Vector3f(cameraPosition);
    }

    /** Met à jour la position de la caméra. */
    public void setCameraPosition(Vector3f position) {
        if (position != null) {
            this.cameraPosition = new Vector3f(position);
        }
    }

    @Override
    public int compare(Entity first, Entity second) {
        if (first == second) return 0;
        if (first == null) return 1;
        if (second == null) return -1;
        // Back-to-front : le plus éloigné d'abord.
        return Double.compare(distanceSquared(second), distanceSquared(first));
    }

    private double distanceSquared(Entity entity) {
        Vector3f position = entity.getPosition();
        double dx = position.x - this.cameraPosition.x;
        double dy = position.y - this.cameraPosition.y;
        double dz = position.z - this.cameraPosition.z;
        return dx * dx + dy * dy + dz * dz;
    }
}
