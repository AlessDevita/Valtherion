package fr.alessdevita.valtherion.block;

/**
 * Bloc de Tremplin - propulse les joueurs vers le haut.
 */
public class SpringboardBlock extends Block {
    private float bouncePower;
    private boolean isCharged;
    private float chargeLevel;

    /**
     * Crée un tremplin.
     */
    public SpringboardBlock() {
        super(BlockType.OBSIDIAN);
        this.bouncePower = 10.0f;
        this.isCharged = true;
        this.chargeLevel = 1.0f;
    }

    @Override
    public void update(float deltaTime) {
        if (isCharged) {
            chargeLevel = Math.min(1.0f, chargeLevel + deltaTime * 0.5f);
        }
    }

    public float getBouncePower() {
        return bouncePower;
    }

    public void setBouncePower(float power) {
        this.bouncePower = power;
    }

    public boolean isCharged() {
        return isCharged;
    }

    public void bounce() {
        if (chargeLevel > 0.5f) {
            chargeLevel = 0.0f;
            isCharged = false;
        }
    }

    public float getChargeLevel() {
        return chargeLevel;
    }

    public void setChargeLevel(float level) {
        this.chargeLevel = Math.max(0.0f, Math.min(1.0f, level));
        this.isCharged = chargeLevel > 0.1f;
    }
}
