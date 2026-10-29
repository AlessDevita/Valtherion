package fr.alessdevita.valtherion.client.shader;

/**
 * Snapshot de l'état vertex d'un {@link fr.alessdevita.valtherion.client.renderer.Tessellator}.
 * <p>
 * Permet de sauvegarder puis de restaurer l'état du Tessellator pour
 * effectuer un rendu différé (ex: re-rendu d'un chunk sans tout recalculer).
 */
public final class TesselatorVertexState {
    private final int drawMode;
    private final boolean hasTexture;
    private final boolean hasColor;
    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;
    private final double u;
    private final double v;

    /**
     * Capture l'état du Tessellator.
     *
     * @param drawMode   le mode de dessin GL (ex: {@code GL_QUADS})
     * @param hasTexture {@code true} si des coordonnées UV sont définies
     * @param hasColor   {@code true} si une couleur est définie
     * @param red        composante rouge [0..1]
     * @param green      composante verte [0..1]
     * @param blue       composante bleue [0..1]
     * @param alpha      composante alpha [0..1]
     * @param u          coordonnée U de texture
     * @param v          coordonnée V de texture
     */
    public TesselatorVertexState(int drawMode, boolean hasTexture, boolean hasColor,
                                 float red, float green, float blue, float alpha,
                                 double u, double v) {
        this.drawMode = drawMode;
        this.hasTexture = hasTexture;
        this.hasColor = hasColor;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.u = u;
        this.v = v;
    }

    public int getDrawMode() {
        return this.drawMode;
    }

    public boolean hasTexture() {
        return this.hasTexture;
    }

    public boolean hasColor() {
        return this.hasColor;
    }

    public float getRed() {
        return this.red;
    }

    public float getGreen() {
        return this.green;
    }

    public float getBlue() {
        return this.blue;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public double getU() {
        return this.u;
    }

    public double getV() {
        return this.v;
    }

    @Override
    public String toString() {
        return "TesselatorVertexState[mode=" + this.drawMode
                + ", texture=" + this.hasTexture
                + ", color=" + this.hasColor
                + ", rgba=(" + this.red + "," + this.green + "," + this.blue + "," + this.alpha + ")"
                + ", uv=(" + this.u + "," + this.v + ")]";
    }
}
