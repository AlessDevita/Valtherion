package fr.alessdevita.valtherion.block;

/**
 * Écho Spectral - bloc qui capture et rejette les échos de magie.
 */
public class SpectralEcho extends Block {
    private float echoIntensity;
    private float echoDelay;
    private boolean hasEcho;

    /**
     * Crée un bloc d'écho spectral.
     */
    public SpectralEcho() {
        super(BlockType.OBSIDIAN);
        this.echoIntensity = 0.0f;
        this.echoDelay = 0.5f;
        this.hasEcho = false;
    }

    @Override
    public void update(float deltaTime) {
        if (hasEcho) {
            echoIntensity = Math.max(0.0f, echoIntensity - deltaTime * 0.3f);
            if (echoIntensity <= 0.0f) {
                hasEcho = false;
            }
        }
    }

    public float getEchoIntensity() {
        return echoIntensity;
    }

    public float getEchoDelay() {
        return echoDelay;
    }

    public void setEchoDelay(float delay) {
        this.echoDelay = delay;
    }

    public boolean hasEcho() {
        return hasEcho;
    }

    public void captureEcho(float intensity) {
        this.echoIntensity = intensity;
        this.hasEcho = true;
    }

    public float releaseEcho() {
        float released = echoIntensity;
        echoIntensity = 0.0f;
        hasEcho = false;
        return released;
    }
}
