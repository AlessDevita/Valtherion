package fr.alessdevita.valtherion.block;

/**
 * Bloc de Temps Fixe - gèle le temps dans sa zone d'effet.
 */
public class FixedTimeBlock extends Block {
    private float timeMultiplier;
    private float affectRadius;
    private boolean isActive;

    /**
     * Crée un bloc de temps fixe.
     */
    public FixedTimeBlock() {
        super(BlockType.OBSIDIAN);
        this.timeMultiplier = 0.0f;
        this.affectRadius = 10.0f;
        this.isActive = true;
    }

    @Override
    public void update(float deltaTime) {
        if (isActive) {
            timeMultiplier = Math.max(0.0f, timeMultiplier - deltaTime * 0.05f);
        }
    }

    public float getTimeMultiplier() {
        return timeMultiplier;
    }

    public void setTimeMultiplier(float multiplier) {
        this.timeMultiplier = Math.max(0.0f, Math.min(1.0f, multiplier));
    }

    public float getAffectRadius() {
        return affectRadius;
    }

    public void setAffectRadius(float radius) {
        this.affectRadius = radius;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void freezeTime() {
        timeMultiplier = 0.0f;
    }

    public void unfreezeTime() {
        timeMultiplier = 1.0f;
    }
}
