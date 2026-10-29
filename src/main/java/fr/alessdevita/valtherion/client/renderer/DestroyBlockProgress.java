package fr.alessdevita.valtherion.client.renderer;

/** État de fissure d'un bloc pendant sa destruction. */
public class DestroyBlockProgress {
    private final int miningPlayerEntityId;
    private final int x, y, z;
    private int partialBlockDamage = -1;
    private int cloudUpdateTick;

    public DestroyBlockProgress(int entityId, int x, int y, int z) {
        this.miningPlayerEntityId = entityId;
        this.x = x; this.y = y; this.z = z;
    }

    public int getPartialBlockDamage() { return this.partialBlockDamage; }
    public void setPartialBlockDamage(int damage) { this.partialBlockDamage = Math.max(-1, Math.min(9, damage)); }
    public int getMiningPlayerEntityId() { return this.miningPlayerEntityId; }
    public int getPositionX() { return this.x; }
    public int getPositionY() { return this.y; }
    public int getPositionZ() { return this.z; }
    public int getCloudUpdateTick() { return this.cloudUpdateTick; }
    public void setCloudUpdateTick(int tick) { this.cloudUpdateTick = tick; }
}
