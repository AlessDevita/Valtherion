package fr.alessdevita.valtherion.client.resources.data;

/**
 * Métadonnées de texture (blur, clamp).
 */
public final class TextureMetadataSection implements IMetadataSection {
    public static final String SECTION_NAME = "texture";

    private final boolean blur;
    private final boolean clamp;

    public TextureMetadataSection(boolean blur, boolean clamp) {
        this.blur = blur;
        this.clamp = clamp;
    }

    public boolean isBlur() {
        return this.blur;
    }

    public boolean isClamp() {
        return this.clamp;
    }
}
