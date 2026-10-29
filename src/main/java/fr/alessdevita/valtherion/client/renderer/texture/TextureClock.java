package fr.alessdevita.valtherion.client.renderer.texture;

/** Sprite d'horloge animée selon l'heure du monde. */
public class TextureClock extends TextureAtlasSprite {
    private double currentAngle;
    private double angleDelta;
    private int frameIndex;

    public TextureClock(String name) {
        super(name);
    }

    public void updateAnimation(long worldTime, boolean isDaytime) {
        double target = (worldTime % 24000L) / 24000.0D;
        if (!isDaytime) target = 0.5D;
        double delta = target - this.currentAngle;
        while (delta < -0.5D) delta += 1.0D;
        while (delta >= 0.5D) delta -= 1.0D;
        this.angleDelta += delta * 0.1D;
        this.angleDelta *= 0.8D;
        this.currentAngle = (this.currentAngle + this.angleDelta) % 1.0D;
        if (this.currentAngle < 0.0D) this.currentAngle += 1.0D;

        int frames = Math.max(1, getFrameTextureData().size());
        this.frameIndex = (int) (this.currentAngle * frames) % frames;
    }

    public int getFrameIndex() {
        return this.frameIndex;
    }

    public double getCurrentAngle() {
        return this.currentAngle;
    }
}
