package fr.alessdevita.valtherion.block;

/**
 * Bloc Rebondissant - bloc élastique qui fait rebondir.
 */
public class BouncingBlock extends Block {
    private float bounceCoefficient;
    private boolean isActive;
    private int bounceCount;

    /**
     * Crée un bloc rebondissant.
     */
    public BouncingBlock() {
        super(BlockType.OBSIDIAN);
        this.bounceCoefficient = 0.8f;
        this.isActive = true;
        this.bounceCount = 0;
    }

    @Override
    public void update(float deltaTime) {
        if (bounceCount > 0) {
            bounceCount--;
        }
    }

    public float getBounceCoefficient() {
        return bounceCoefficient;
    }

    public void setBounceCoefficient(float coefficient) {
        this.bounceCoefficient = Math.max(0.0f, Math.min(1.0f, coefficient));
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void bounce() {
        if (isActive) {
            bounceCount = 2;
        }
    }

    public int getBounceCount() {
        return bounceCount;
    }

    public boolean isBouncing() {
        return bounceCount > 0;
    }
}
