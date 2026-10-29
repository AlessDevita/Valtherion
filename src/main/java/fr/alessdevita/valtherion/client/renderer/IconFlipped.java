package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.client.renderer.texture.TextureAtlasSprite;

/** Sprite proxy qui inverse les coordonnées U et/ou V d'une icône. */
public class IconFlipped extends TextureAtlasSprite {
    private final TextureAtlasSprite original;
    private final boolean flipU, flipV;

    public IconFlipped(TextureAtlasSprite original, boolean flipU, boolean flipV) {
        super(original == null ? "missingno" : original.getIconName() + "_flipped");
        if (original == null) throw new IllegalArgumentException("Icône source absente.");
        this.original = original;
        this.flipU = flipU; this.flipV = flipV;
    }

    @Override public float getMinU() { return this.flipU ? this.original.getMaxU() : this.original.getMinU(); }
    @Override public float getMaxU() { return this.flipU ? this.original.getMinU() : this.original.getMaxU(); }
    @Override public float getMinV() { return this.flipV ? this.original.getMaxV() : this.original.getMinV(); }
    @Override public float getMaxV() { return this.flipV ? this.original.getMinV() : this.original.getMaxV(); }
    @Override public int getIconWidth() { return this.original.getIconWidth(); }
    @Override public int getIconHeight() { return this.original.getIconHeight(); }
}
