package fr.alessdevita.valtherion.block;

/**
 * Bloc de Corruption Vivante - bloc corrupteur qui se propage et corrompt son environnement.
 */
public class LivingCorruptionBlock extends Block {
    private float corruptionLevel;
    private float spreadRate;
    private int affectRadius;
    private boolean isActive;

    /**
     * Crée un bloc de corruption vivante.
     */
    public LivingCorruptionBlock() {
        super(BlockType.OBSIDIAN);
        this.corruptionLevel = 0.5f;
        this.spreadRate = 0.3f;
        this.affectRadius = 8;
        this.isActive = true;
    }

    @Override
    public void update(float deltaTime) {
        if (isActive) {
            corruptionLevel = Math.min(1.0f, corruptionLevel + deltaTime * spreadRate);
        }
    }

    public float getCorruptionLevel() {
        return corruptionLevel;
    }

    public void setCorruptionLevel(float level) {
        this.corruptionLevel = Math.max(0.0f, Math.min(1.0f, level));
    }

    public float getSpreadRate() {
        return spreadRate;
    }

    public void setSpreadRate(float rate) {
        this.spreadRate = rate;
    }

    public int getAffectRadius() {
        return affectRadius;
    }

    public void setAffectRadius(int radius) {
        this.affectRadius = radius;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void spread() {
        corruptionLevel = Math.min(1.0f, corruptionLevel + 0.05f);
    }

    public void purify(float amount) {
        corruptionLevel = Math.max(0.0f, corruptionLevel - amount);
    }
}
