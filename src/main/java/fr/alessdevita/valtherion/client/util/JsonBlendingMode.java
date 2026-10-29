package fr.alessdevita.valtherion.client.util;

/**
 * Mode de blending OpenGL lu depuis du JSON (ex: dans les fichiers
 * de modèle de bloc pour le rendu translucide).
 * <p>
 * Représente une paire source/destination comme
 * {@code glBlendFunc(srcFactor, dstFactor)}.
 */
public final class JsonBlendingMode {
    private final int srcFactor;
    private final int dstFactor;
    private final boolean separateAlpha;

    /**
     * Construit un mode de blending.
     *
     * @param srcFactor     le facteur source (ex: {@code GL_SRC_ALPHA})
     * @param dstFactor     le facteur destination (ex: {@code GL_ONE_MINUS_SRC_ALPHA})
     * @param separateAlpha {@code true} si l'alpha utilise des facteurs séparés
     */
    public JsonBlendingMode(int srcFactor, int dstFactor, boolean separateAlpha) {
        this.srcFactor = srcFactor;
        this.dstFactor = dstFactor;
        this.separateAlpha = separateAlpha;
    }

    public JsonBlendingMode(int srcFactor, int dstFactor) {
        this(srcFactor, dstFactor, false);
    }

    /** @return le facteur source. */
    public int getSrcFactor() {
        return this.srcFactor;
    }

    /** @return le facteur destination. */
    public int getDstFactor() {
        return this.dstFactor;
    }

    /** @return {@code true} si l'alpha utilise des facteurs séparés. */
    public boolean isSeparateAlpha() {
        return this.separateAlpha;
    }

    /**
     * Convertit un nom de facteur en constante OpenGL.
     *
     * @param name le nom (ex: "src_alpha", "one_minus_src_alpha")
     * @return la constante OpenGL, ou {@code GL_ONE} par défaut
     */
    public static int parseFactor(String name) {
        if (name == null) return org.lwjgl.opengl.GL11.GL_ONE;
        return switch (name.toLowerCase()) {
            case "zero" -> org.lwjgl.opengl.GL11.GL_ZERO;
            case "one" -> org.lwjgl.opengl.GL11.GL_ONE;
            case "src_color" -> org.lwjgl.opengl.GL11.GL_SRC_COLOR;
            case "one_minus_src_color" -> org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_COLOR;
            case "src_alpha" -> org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
            case "one_minus_src_alpha" -> org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
            case "dst_alpha" -> org.lwjgl.opengl.GL11.GL_DST_ALPHA;
            case "one_minus_dst_alpha" -> org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_ALPHA;
            case "dst_color" -> org.lwjgl.opengl.GL11.GL_DST_COLOR;
            case "one_minus_dst_color" -> org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_COLOR;
            case "constant_color" -> org.lwjgl.opengl.GL14.GL_CONSTANT_COLOR;
            case "one_minus_constant_color" -> org.lwjgl.opengl.GL14.GL_ONE_MINUS_CONSTANT_COLOR;
            case "constant_alpha" -> org.lwjgl.opengl.GL14.GL_CONSTANT_ALPHA;
            case "one_minus_constant_alpha" -> org.lwjgl.opengl.GL14.GL_ONE_MINUS_CONSTANT_ALPHA;
            default -> org.lwjgl.opengl.GL11.GL_ONE;
        };
    }

    /**
     * Active ce mode de blending via {@code glBlendFunc}.
     */
    public void apply() {
        if (this.separateAlpha) {
            org.lwjgl.opengl.GL14.glBlendFuncSeparate(
                    this.srcFactor, this.dstFactor,
                    org.lwjgl.opengl.GL11.GL_SRC_ALPHA, org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            org.lwjgl.opengl.GL11.glBlendFunc(this.srcFactor, this.dstFactor);
        }
    }

    @Override
    public String toString() {
        return "JsonBlendingMode[src=" + this.srcFactor
                + ", dst=" + this.dstFactor
                + ", separateAlpha=" + this.separateAlpha + "]";
    }
}
