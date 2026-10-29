package fr.alessdevita.valtherion.world;

/** Immutable configuration used when creating a world. */
public class WorldSettings {
    private final long seed;
    private final WorldType worldType;
    private final boolean generateStructures;
    private final boolean hardcore;

    public WorldSettings() { this(0L); }
    public WorldSettings(long seed) { this(seed, WorldType.NORMAL, true, false); }
    public WorldSettings(long seed, WorldType worldType, boolean generateStructures, boolean hardcore) {
        this.seed = seed;
        this.worldType = worldType == null ? WorldType.NORMAL : worldType;
        this.generateStructures = generateStructures;
        this.hardcore = hardcore;
    }
    public long getSeed() { return seed; }
    public WorldType getWorldType() { return worldType; }
    public boolean areStructuresEnabled() { return generateStructures; }
    public boolean isHardcore() { return hardcore; }
}
