package fr.alessdevita.valtherion.client.renderer.texture;

/** Sprite de boussole orientée vers un point d'intérêt. */
public class TextureCompass extends TextureAtlasSprite {
    private double currentAngle;
    private double angleDelta;
    private int frameIndex;

    public TextureCompass(String name) {
        super(name);
    }

    public void updateCompass(double playerX, double playerZ, double playerYaw,
                              double targetX, double targetZ, boolean hasTarget) {
        double targetAngle;
        if (hasTarget) {
            double dx = targetX - playerX;
            double dz = targetZ - playerZ;
            targetAngle = Math.atan2(dz, dx) - Math.toRadians(playerYaw) - Math.PI * 0.5D;
        } else {
            targetAngle = Math.random() * Math.PI * 2.0D;
        }

        double delta = targetAngle - this.currentAngle;
        while (delta < -Math.PI) delta += Math.PI * 2.0D;
        while (delta >= Math.PI) delta -= Math.PI * 2.0D;
        this.angleDelta += delta * 0.1D;
        this.angleDelta *= 0.8D;
        this.currentAngle += this.angleDelta;

        int frames = Math.max(1, getFrameTextureData().size());
        double normalized = (this.currentAngle / (Math.PI * 2.0D)) % 1.0D;
        if (normalized < 0.0D) normalized += 1.0D;
        this.frameIndex = (int) (normalized * frames) % frames;
    }

    public int getFrameIndex() {
        return this.frameIndex;
    }

    public double getCurrentAngle() {
        return this.currentAngle;
    }
}
