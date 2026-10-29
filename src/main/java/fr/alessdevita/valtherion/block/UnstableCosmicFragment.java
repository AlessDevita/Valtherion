package fr.alessdevita.valtherion.block;

/**
 * Fragment Cosmique Instable - bloc magique contenant une énergie cosmique instable.
 */
public class UnstableCosmicFragment extends Block {
    private float instability;
    private float explosionThreshold;
    private int explosionRadius;

    /**
     * Crée un fragment cosmique instable.
     */
    public UnstableCosmicFragment() {
        super(BlockType.OBSIDIAN);
        this.instability = 0.5f;
        this.explosionThreshold = 1.0f;
        this.explosionRadius = 10;
    }

    @Override
    public void update(float deltaTime) {
        instability += deltaTime * 0.15f;
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

    public float getExplosionThreshold() {
        return explosionThreshold;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public boolean willExplode() {
        return instability >= explosionThreshold;
    }

    private void explode() {
        instability = 0.0f;
    }

    public void increaseInstability(float amount) {
        instability += amount;
    }
}
