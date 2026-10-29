package fr.alessdevita.valtherion.block;

/**
 * Bloc de Cristal Instable - cristal qui peut exploser.
 */
public class UnstableCrystalBlock extends Block {
    private float instability;
    private float explosionThreshold;
    private int explosionRadius;
    private int xpDropped;

    /**
     * Crée un bloc de cristal instable.
     */
    public UnstableCrystalBlock() {
        super(BlockType.OBSIDIAN);
        this.instability = 0.3f;
        this.explosionThreshold = 1.0f;
        this.explosionRadius = 5;
        this.xpDropped = 15;
    }

    @Override
    public void update(float deltaTime) {
        instability += deltaTime * 0.1f;
        if (instability >= explosionThreshold) {
            explode();
        }
    }

    public float getInstability() {
        return instability;
    }

    public void setInstability(float instability) {
        this.instability = instability;
    }

    public boolean willExplode() {
        return instability >= explosionThreshold;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    private void explode() {
        instability = 0.0f;
    }

    public int getXpDropped() {
        return xpDropped;
    }
}
