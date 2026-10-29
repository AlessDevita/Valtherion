package fr.alessdevita.valtherion.block;

/**
 * Autel Abandonné - bloc représentant un ancien autel oublié.
 */
public class AbandonedAltar extends Block {
    private String deity;
    private String lastPrayer;
    private long abandonedSince;
    private boolean isRestored;

    /**
     * Crée un autel abandonné.
     */
    public AbandonedAltar() {
        super(BlockType.STONE);
        this.deity = "Dieu Oublié";
        this.lastPrayer = "";
        this.abandonedSince = System.currentTimeMillis();
        this.isRestored = false;
    }

    @Override
    public void update(float deltaTime) {
    }

    public String getDeity() {
        return deity;
    }

    public void setDeity(String deity) {
        this.deity = deity;
    }

    public String getLastPrayer() {
        return lastPrayer;
    }

    public void setLastPrayer(String prayer) {
        this.lastPrayer = prayer;
    }

    public long getAbandonedSince() {
        return abandonedSince;
    }

    public boolean isRestored() {
        return isRestored;
    }

    public void restore() {
        this.isRestored = true;
    }
}
