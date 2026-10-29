package fr.alessdevita.valtherion.client.resources.data;

import java.util.List;

/**
 * Métadonnées d'animation pour une texture.
 * Décrit les frames et leurs durées.
 */
public final class AnimationMetadataSection implements IMetadataSection {
    public static final String SECTION_NAME = "animation";

    private final int frameWidth;
    private final int frameHeight;
    private final int defaultFrameTime;
    private final List<AnimationFrame> animationFrames;
    private final boolean interpolate;

    public AnimationMetadataSection(int frameWidth, int frameHeight, int defaultFrameTime,
                                    List<AnimationFrame> animationFrames, boolean interpolate) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.defaultFrameTime = defaultFrameTime;
        this.animationFrames = animationFrames;
        this.interpolate = interpolate;
    }

    public int getFrameWidth() {
        return this.frameWidth;
    }

    public int getFrameHeight() {
        return this.frameHeight;
    }

    public int getDefaultFrameTime() {
        return this.defaultFrameTime;
    }

    public List<AnimationFrame> getAnimationFrames() {
        return this.animationFrames;
    }

    public boolean isInterpolate() {
        return this.interpolate;
    }

    public int getFrameCount() {
        return this.animationFrames == null ? 0 : this.animationFrames.size();
    }

    public int getFrameTime(int frame) {
        if (this.animationFrames == null || frame < 0 || frame >= this.animationFrames.size()) {
            return this.defaultFrameTime;
        }
        AnimationFrame af = this.animationFrames.get(frame);
        return af.hasCustomTime() ? af.getFrameTime() : this.defaultFrameTime;
    }

    public int getFrameIndex(int frame) {
        if (this.animationFrames == null || frame < 0 || frame >= this.animationFrames.size()) {
            return frame;
        }
        return this.animationFrames.get(frame).getFrameIndex();
    }
}
