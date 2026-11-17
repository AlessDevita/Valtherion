package fr.alessdevita.valtherion.world.storage;

import fr.alessdevita.valtherion.world.WorldSettings;

public class WorldInfo {
    private String worldName;
    private long seed;
    private String gameMode;
    private long lastPlayed;

    public WorldInfo() {
        this("World", 0L);
    }

    public WorldInfo(WorldSettings settings, String worldName) {
        this(worldName, settings == null ? 0L : settings.getSeed());
    }

    public WorldInfo(String worldName, long seed) {
        this.worldName = worldName == null || worldName.trim().isEmpty() ? "World" : worldName.trim();
        this.seed = seed;
        this.gameMode = "Survie";
        this.lastPlayed = System.currentTimeMillis();
    }

    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(String worldName) {
        if (worldName != null && !worldName.trim().isEmpty()) {
            this.worldName = worldName.trim();
        }
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public String getGameMode() {
        return this.gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public long getLastPlayed() {
        return this.lastPlayed;
    }

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
}
