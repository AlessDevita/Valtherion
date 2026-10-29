package fr.alessdevita.valtherion.client.resources.data;

/**
 * Métadonnées de police de caractères.
 */
public final class FontMetadataSection implements IMetadataSection {
    public static final String SECTION_NAME = "font";

    private final float[] uvTop;
    private final float[] uvBottom;
    private final float[] uvLeft;
    private final float[] uvRight;
    private final float[] offsetX;
    private final float[] offsetY;
    private final float[] advanceWidth;

    public FontMetadataSection(float[] uvTop, float[] uvBottom, float[] uvLeft, float[] uvRight,
                               float[] offsetX, float[] offsetY, float[] advanceWidth) {
        this.uvTop = uvTop;
        this.uvBottom = uvBottom;
        this.uvLeft = uvLeft;
        this.uvRight = uvRight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.advanceWidth = advanceWidth;
    }

    public float[] getUvTop() { return this.uvTop; }
    public float[] getUvBottom() { return this.uvBottom; }
    public float[] getUvLeft() { return this.uvLeft; }
    public float[] getUvRight() { return this.uvRight; }
    public float[] getOffsetX() { return this.offsetX; }
    public float[] getOffsetY() { return this.offsetY; }
    public float[] getAdvanceWidth() { return this.advanceWidth; }
}
