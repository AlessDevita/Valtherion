package fr.alessdevita.valtherion.client.resources.data;

/**
 * Une image clé dans une animation de texture.
 */
public final class AnimationFrame implements IMetadataSection {
    private final int frameIndex;
    private final int frameTime;

    public AnimationFrame(int frameIndex) {
        this(frameIndex, -1);
    }

    public AnimationFrame(int frameIndex, int frameTime) {
        this.frameIndex = frameIndex;
        this.frameTime = frameTime;
    }

    public int getFrameIndex() {
        return this.frameIndex;
    }

    public int getFrameTime() {
        return this.frameTime;
    }

    public boolean hasCustomTime() {
        return this.frameTime >= 0;
    }
}
