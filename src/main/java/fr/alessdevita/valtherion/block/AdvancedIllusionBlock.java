package fr.alessdevita.valtherion.block;

/**
 * Bloc d'Illusion Avancée - crée des illusions complexes et des mirages.
 */
public class AdvancedIllusionBlock extends Block {
    private float illusionStrength;
    private boolean isActive;
    private int illusionType;

    /**
     * Crée un bloc d'illusion avancée.
     */
    public AdvancedIllusionBlock() {
        super(BlockType.OBSIDIAN);
        this.illusionStrength = 0.8f;
        this.isActive = true;
        this.illusionType = 0;
    }

    @Override
    public void update(float deltaTime) {
        if (isActive) {
            illusionStrength = (float) (0.7f + 0.3f * Math.sin(System.currentTimeMillis() * 0.005f));
        }
    }

    public float getIllusionStrength() {
        return illusionStrength;
    }

    public void setIllusionStrength(float strength) {
        this.illusionStrength = Math.max(0.0f, Math.min(1.0f, strength));
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public int getIllusionType() {
        return illusionType;
    }

    public void setIllusionType(int type) {
        this.illusionType = type;
    }

    public void toggleIllusion() {
        isActive = !isActive;
    }
}
