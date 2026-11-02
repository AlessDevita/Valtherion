package fr.alessdevita.valtherion.world;

/**
 * Énumération des types de ciel pour l'éclairage du monde.
 */
public enum EnumSkyBlock {
    SKY("Ciel", true),
    UNDERGROUND("Souterrain", false);

    private final String displayName;
    private final boolean hasSky;

    EnumSkyBlock(String displayName, boolean hasSky) {
        this.displayName = displayName;
        this.hasSky = hasSky;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean hasSky() {
        return hasSky;
    }
}
