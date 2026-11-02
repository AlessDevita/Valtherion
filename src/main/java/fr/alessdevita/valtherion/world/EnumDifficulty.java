package fr.alessdevita.valtherion.world;

/**
 * Énumération des niveaux de difficulté du monde.
 */
public enum EnumDifficulty {
    PEACEFUL(0, "Paix", false, false),
    EASY(1, "Facile", true, false),
    NORMAL(2, "Normal", true, true),
    HARD(3, "Difficile", true, true);

    private final int id;
    private final String displayName;
    private final boolean canMobSpawn;
    private final boolean canHurtPlayer;

    EnumDifficulty(int id, String displayName, boolean canMobSpawn, boolean canHurtPlayer) {
        this.id = id;
        this.displayName = displayName;
        this.canMobSpawn = canMobSpawn;
        this.canHurtPlayer = canHurtPlayer;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canMobSpawn() {
        return canMobSpawn;
    }

    public boolean canHurtPlayer() {
        return canHurtPlayer;
    }

    /**
     * Récupère la difficulté par son ID.
     * @param id ID de la difficulté
     * @return Difficulté ou NORMAL par défaut
     */
    public static EnumDifficulty byId(int id) {
        for (EnumDifficulty difficulty : values()) {
            if (difficulty.id == id) {
                return difficulty;
            }
        }
        return NORMAL;
    }
}
