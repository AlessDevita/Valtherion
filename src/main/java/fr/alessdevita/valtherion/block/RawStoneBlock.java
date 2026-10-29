package fr.alessdevita.valtherion.block;

/**
 * Bloc de Pierre Brute - pierre de base du monde.
 */
public class RawStoneBlock extends Block {
    private float hardness;
    private int xpDropped;
    private boolean canMine;

    /**
     * Crée un bloc de pierre brute.
     */
    public RawStoneBlock() {
        super(BlockType.STONE);
        this.hardness = 1.5f;
        this.xpDropped = 10;
        this.canMine = true;
    }

    @Override
    public void update(float deltaTime) {
    }

    public float getHardness() {
        return hardness;
    }

    public void setHardness(float hardness) {
        this.hardness = Math.max(0.1f, hardness);
    }

    public int getXpDropped() {
        return xpDropped;
    }

    public boolean canMine() {
        return canMine;
    }

    public void setCanMine(boolean canMine) {
        this.canMine = canMine;
    }
}
