package fr.alessdevita.valtherion.block;

/**
 * Statue Cassée - bloc représentant une statue ancienne brisée.
 */
public class BrokenStatue extends Block {
    private String representedFigure;
    private float intactness;
    private String originalPurpose;
    private boolean canBeRepaired;

    /**
     * Crée une statue cassée.
     */
    public BrokenStatue() {
        super(BlockType.STONE);
        this.representedFigure = "Figure Inconnue";
        this.intactness = 0.4f;
        this.originalPurpose = "Commémorative";
        this.canBeRepaired = true;
    }

    @Override
    public void update(float deltaTime) {
    }

    public String getRepresentedFigure() {
        return representedFigure;
    }

    public void setRepresentedFigure(String figure) {
        this.representedFigure = figure;
    }

    public float getIntactness() {
        return intactness;
    }

    public void setIntactness(float intactness) {
        this.intactness = Math.max(0.0f, Math.min(1.0f, intactness));
    }

    public String getOriginalPurpose() {
        return originalPurpose;
    }

    public boolean canBeRepaired() {
        return canBeRepaired;
    }

    public void repair() {
        if (canBeRepaired) {
            intactness = 1.0f;
        }
    }
}
