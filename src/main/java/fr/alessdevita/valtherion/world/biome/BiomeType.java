package fr.alessdevita.valtherion.world.biome;

/**
 * Énumération des types de biomes dans Valtherion.
 */
public enum BiomeType {
    PLAINS("Plaine", "Un biome plat et ouvert avec de l'herbe et des fleurs."),
    FOREST("Forêt", "Un biome boisé avec des arbres et des buissons."),
    MOUNTAIN("Montagne", "Un biome montagneux avec des rochers et des pentes raides."),
    DESERT("Désert", "Un biome sec avec du sable et des cactus."),
    OCEAN("Océan", "Un biome aquatique profond avec de l'eau salée."),
    RIVER("Rivière", "Un biome aquatique peu profond avec de l'eau douce."),
    SNOW("Neige", "Un biome froid avec de la neige et de la glace."),
    SWAMP("Marais", "Un biome humide avec de l'eau stagnante et des arbres morts."),
    JUNGLE("Jungle", "Un biome dense avec des arbres hauts et une végétation épaisse."),
    NETHER("Nether", "Un biome infernal avec de la lave et des roches sombres.");

    private final String displayName;
    private final String description;

    BiomeType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}