package fr.alessdevita.valtherion.block;

/**
 * Ruine Interactive - bloc narratif qui raconte une histoire du passé.
 */
public class InteractiveRuin extends Block {
    private String narrative;
    private boolean hasBeenInteracted;
    private float damageLevel;

    /**
     * Crée une ruine interactive.
     */
    public InteractiveRuin() {
        super(BlockType.COBBLESTONE);
        this.narrative = "Une ancienne ruine du passé...";
        this.hasBeenInteracted = false;
        this.damageLevel = 0.7f;
    }

    @Override
    public void update(float deltaTime) {
        damageLevel = Math.min(1.0f, damageLevel + deltaTime * 0.01f);
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public boolean hasBeenInteracted() {
        return hasBeenInteracted;
    }

    public void interact() {
        hasBeenInteracted = true;
    }

    public float getDamageLevel() {
        return damageLevel;
    }
}
