package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui génère des îles.
 * Utilisée pour les mondes de type île.
 */
public class GenLayerIsland extends GenLayer {

    private final int islandCenterX;
    private final int islandCenterZ;
    private final int islandRadius;

    /**
     * Crée une couche d'îles.
     * @param seed Graine pour la génération aléatoire
     * @param islandCenterX Centre X de l'île
     * @param islandCenterZ Centre Z de l'île
     * @param islandRadius Rayon de l'île
     */
    public GenLayerIsland(long seed, int islandCenterX, int islandCenterZ, int islandRadius) {
        super(seed);
        this.islandCenterX = islandCenterX;
        this.islandCenterZ = islandCenterZ;
        this.islandRadius = islandRadius;
    }

    @Override
    protected int getValue(int x, int z) {
        int dx = x - islandCenterX;
        int dz = z - islandCenterZ;
        int distance = dx * dx + dz * dz;
        int radiusSquared = islandRadius * islandRadius;

        if (distance <= radiusSquared) {
            // À l'intérieur de l'île
            if (distance <= radiusSquared / 4) {
                return BiomeType.PLAINS.ordinal();
            } else if (distance <= radiusSquared / 2) {
                return BiomeType.FOREST.ordinal();
            } else {
                return BiomeType.MOUNTAIN.ordinal();
            }
        } else {
            // À l'extérieur de l'île (océan)
            return BiomeType.OCEAN.ordinal();
        }
    }
}
