package fr.alessdevita.valtherion.block.material;

/** Physical material traits used by block interaction and rendering code. */
public final class Material {
    public static final Material air = new Material(false, false);
    public static final Material rock = new Material(true, true);
    public static final Material water = new Material(false, true);

    private final boolean solid;
    private final boolean opaque;

    public Material() {
        this(true, true);
    }

    public Material(boolean solid, boolean opaque) {
        this.solid = solid;
        this.opaque = opaque;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public boolean isOpaque() {
        return this.opaque;
    }
}
