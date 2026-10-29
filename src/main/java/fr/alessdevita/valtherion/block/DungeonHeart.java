package fr.alessdevita.valtherion.block;

/**
 * Cœur de Donjon - bloc signature qui génère et maintient les donjons.
 */
public class DungeonHeart extends Block {
    private int dungeonRadius;
    private float healingPower;
    private float spawnRate;
    private boolean isCorrupted;

    /**
     * Crée un cœur de donjon.
     */
    public DungeonHeart() {
        super(BlockType.OBSIDIAN);
        this.dungeonRadius = 20;
        this.healingPower = 0.1f;
        this.spawnRate = 1.0f;
        this.isCorrupted = false;
    }

    @Override
    public void update(float deltaTime) {
        if (!isCorrupted) {
            healingPower = Math.min(1.0f, healingPower + deltaTime * 0.05f);
        } else {
            spawnRate = Math.min(2.0f, spawnRate + deltaTime * 0.1f);
        }
    }

    public int getDungeonRadius() {
        return dungeonRadius;
    }

    public void setDungeonRadius(int radius) {
        this.dungeonRadius = radius;
    }

    public float getHealingPower() {
        return healingPower;
    }

    public float getSpawnRate() {
        return spawnRate;
    }

    public boolean isCorrupted() {
        return isCorrupted;
    }

    public void setCorrupted(boolean corrupted) {
        this.isCorrupted = corrupted;
    }

    public void spawnMob() {
        if (spawnRate > 0.5f) {
            spawnRate -= 0.1f;
        }
    }
}
