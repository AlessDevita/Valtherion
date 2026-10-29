package fr.alessdevita.valtherion.client.renderer;

import java.util.ArrayList;
import java.util.List;

/** File de display lists de chunks à dessiner pour une passe. */
public class RenderList {
    private final List<Integer> displayLists = new ArrayList<>();
    private double viewX, viewY, viewZ;

    public void setupRenderList(double x, double y, double z) {
        this.viewX = x; this.viewY = y; this.viewZ = z;
        this.displayLists.clear();
    }

    public void renderChunk(WorldRenderer renderer, int pass) {
        if (renderer == null || !renderer.isInFrustum() || renderer.skipAllRenderPasses()) return;
        if (renderer.needsUpdate()) return;
        this.displayLists.add(renderer.getDisplayList());
    }

    public void renderChunkLayer(int pass) {
        for (int displayList : this.displayLists) {
            if (displayList != 0) org.lwjgl.opengl.GL11.glCallList(displayList);
        }
        this.displayLists.clear();
    }

    public int getCount() { return this.displayLists.size(); }
    public double getViewX() { return this.viewX; }
    public double getViewY() { return this.viewY; }
    public double getViewZ() { return this.viewZ; }
}
