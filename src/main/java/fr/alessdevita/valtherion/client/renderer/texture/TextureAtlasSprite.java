package fr.alessdevita.valtherion.client.renderer.texture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Rectangle d'une image dans une texture atlas. */
public class TextureAtlasSprite implements ITickableTextureObject {
    private final String iconName;
    private float minU, maxU = 1F, minV, maxV = 1F;
    private int width = 16, height = 16, glTextureId;
    private int frameCounter, tickCounter;
    private final List<int[][]> frames = new ArrayList<>();

    public TextureAtlasSprite(String name) { this.iconName = name == null ? "" : name; }
    public String getIconName() { return this.iconName; }
    public float getMinU() { return this.minU; }
    public float getMaxU() { return this.maxU; }
    public float getMinV() { return this.minV; }
    public float getMaxV() { return this.maxV; }
    public int getIconWidth() { return this.width; }
    public int getIconHeight() { return this.height; }

    public void initSprite(int atlasWidth, int atlasHeight, int x, int y, boolean rotated) {
        if (atlasWidth <= 0 || atlasHeight <= 0) throw new IllegalArgumentException("Dimensions d'atlas invalides.");
        this.minU = (float) x / atlasWidth;
        this.maxU = (float) (x + this.width) / atlasWidth;
        this.minV = (float) y / atlasHeight;
        this.maxV = (float) (y + this.height) / atlasHeight;
    }

    public void setIconWidth(int width) { this.width = Math.max(1, width); }
    public void setIconHeight(int height) { this.height = Math.max(1, height); }
    public void setUV(float minU, float maxU, float minV, float maxV) {
        this.minU = minU; this.maxU = maxU; this.minV = minV; this.maxV = maxV;
    }
    public void setFramesTextureData(List<int[][]> frameData) {
        this.frames.clear();
        if (frameData != null) this.frames.addAll(frameData);
        this.frameCounter = 0; this.tickCounter = 0;
    }
    public List<int[][]> getFrameTextureData() { return Collections.unmodifiableList(this.frames); }

    @Override
    public void tick() {
        if (this.frames.size() > 1 && ++this.tickCounter >= 2) {
            this.tickCounter = 0;
            this.frameCounter = (this.frameCounter + 1) % this.frames.size();
        }
    }

    @Override
    public int getGlTextureId() { return this.glTextureId; }
    public void setGlTextureId(int id) { this.glTextureId = id; }
    @Override
    public boolean isLoaded() { return this.glTextureId != 0; }
    @Override
    public void loadTexture() { }
    @Override
    public void deleteGlTexture() { this.glTextureId = 0; }
}
