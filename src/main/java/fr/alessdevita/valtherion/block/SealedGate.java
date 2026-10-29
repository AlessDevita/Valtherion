package fr.alessdevita.valtherion.block;

/**
 * Porte Scellée - bloc qui scelle une zone ou un secret.
 */
public class SealedGate extends Block {
    private String secretBehind;
    private boolean isSealed;
    private float sealStrength;
    private String keyRequired;

    /**
     * Crée une porte scellée.
     */
    public SealedGate() {
        super(BlockType.OBSIDIAN);
        this.secretBehind = "Un secret oublié";
        this.isSealed = true;
        this.sealStrength = 1.0f;
        this.keyRequired = "None";
    }

    @Override
    public void update(float deltaTime) {
        if (!isSealed) {
            sealStrength = Math.max(0.0f, sealStrength - deltaTime * 0.1f);
        }
    }

    public String getSecretBehind() {
        return secretBehind;
    }

    public void setSecretBehind(String secret) {
        this.secretBehind = secret;
    }

    public boolean isSealed() {
        return isSealed;
    }

    public void seal() {
        this.isSealed = true;
        this.sealStrength = 1.0f;
    }

    public void unseal() {
        this.isSealed = false;
    }

    public float getSealStrength() {
        return sealStrength;
    }

    public String getKeyRequired() {
        return keyRequired;
    }

    public void setKeyRequired(String key) {
        this.keyRequired = key;
    }
}
