package fr.alessdevita.valtherion.client.renderer;

import org.lwjgl.opengl.GL11;

/** Petit accumulateur de sommets compatible avec l'ancienne API du moteur. */
public final class Tessellator {
    public static final Tessellator instance = new Tessellator();
    private int drawMode = GL11.GL_QUADS;
    private boolean drawing;
    private boolean hasTexture;
    private boolean hasColor;
    private float red = 1F, green = 1F, blue = 1F, alpha = 1F;
    private double u, v;

    private Tessellator() { }

    public void startDrawingQuads() { startDrawing(GL11.GL_QUADS); }
    public void startDrawing(int mode) {
        if (this.drawing) throw new IllegalStateException("Un dessin Tessellator est déjà ouvert.");
        this.drawMode = mode;
        this.hasTexture = false;
        this.hasColor = false;
        this.drawing = true;
        GL11.glBegin(mode);
    }

    public void addVertex(double x, double y, double z) {
        ensureDrawing();
        GL11.glVertex3d(x, y, z);
    }

    public void addVertexWithUV(double x, double y, double z, double u, double v) {
        ensureDrawing();
        GL11.glTexCoord2d(u, v);
        GL11.glVertex3d(x, y, z);
        this.hasTexture = true;
    }

    public void setTextureUV(double u, double v) {
        ensureDrawing();
        GL11.glTexCoord2d(u, v);
        this.u = u;
        this.v = v;
        this.hasTexture = true;
    }

    public void setColorOpaque_I(int color) {
        setColorRGBA_I(color, 255);
    }

    public void setColorRGBA_I(int color, int alpha) {
        setColorRGBA((color >> 16) & 255, (color >> 8) & 255, color & 255, alpha);
    }

    public void setColorRGBA(int red, int green, int blue, int alpha) {
        ensureDrawing();
        this.red = clamp(red) / 255F;
        this.green = clamp(green) / 255F;
        this.blue = clamp(blue) / 255F;
        this.alpha = clamp(alpha) / 255F;
        GL11.glColor4f(this.red, this.green, this.blue, this.alpha);
        this.hasColor = true;
    }

    public int draw() {
        ensureDrawing();
        GL11.glEnd();
        this.drawing = false;
        GL11.glColor4f(1F, 1F, 1F, 1F);
        return 0;
    }

    public boolean isDrawing() { return this.drawing; }
    public boolean hasTexture() { return this.hasTexture; }
    public boolean hasColor() { return this.hasColor; }
    public double getTextureU() { return this.u; }
    public double getTextureV() { return this.v; }
    public int getDrawMode() { return this.drawMode; }

    private void ensureDrawing() {
        if (!this.drawing) throw new IllegalStateException("Appeler startDrawing avant d'ajouter des sommets.");
    }
    private static int clamp(int value) { return Math.max(0, Math.min(255, value)); }
}
