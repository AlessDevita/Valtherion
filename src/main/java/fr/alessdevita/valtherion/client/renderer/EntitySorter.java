package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.entity.Entity;
import org.joml.Vector3f;

import java.util.Comparator;

/** Ordre de rendu transparent : les entités les plus éloignées d'abord. */
public class EntitySorter implements Comparator<Entity> {
    private final Vector3f cameraPosition;

    public EntitySorter(Vector3f cameraPosition) {
        this.cameraPosition = cameraPosition == null ? new Vector3f() : new Vector3f(cameraPosition);
    }

    @Override
    public int compare(Entity first, Entity second) {
        if (first == second) return 0;
        if (first == null) return 1;
        if (second == null) return -1;
        return Double.compare(distanceSquared(second), distanceSquared(first));
    }

    private double distanceSquared(Entity entity) {
        Vector3f position = entity.getPosition();
        double x = position.x - this.cameraPosition.x;
        double y = position.y - this.cameraPosition.y;
        double z = position.z - this.cameraPosition.z;
        return x * x + y * y + z * z;
    }
}
