package fr.alessdevita.valtherion.client.renderer;

import java.util.Comparator;

/** Trie les chunks par distance décroissante depuis le point de vue. */
public class RenderSorter implements Comparator<WorldRenderer> {
    private double x, y, z;
    public RenderSorter() { }
    public RenderSorter(double x, double y, double z) { setPosition(x, y, z); }
    public void setPosition(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }

    @Override
    public int compare(WorldRenderer first, WorldRenderer second) {
        if (first == second) return 0;
        if (first == null) return 1;
        if (second == null) return -1;
        return Double.compare(distanceSquared(second), distanceSquared(first));
    }

    private double distanceSquared(WorldRenderer renderer) {
        double dx = renderer.getPosX() + 8 - this.x;
        double dy = renderer.getPosY() + 128 - this.y;
        double dz = renderer.getPosZ() + 8 - this.z;
        return dx * dx + dy * dy + dz * dz;
    }
}
