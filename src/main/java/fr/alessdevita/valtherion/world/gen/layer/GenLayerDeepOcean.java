package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui crée des océans profonds.
 */
public class GenLayerDeepOcean extends GenLayer {

    /**
     * Crée une couche d'océans profonds.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente des biomes
     */
    public GenLayerDeepOcean(long seed, GenLayer parentLayer) {
        super(seed, parentLayer);
    }

    @Override
    protected int getValue(int x, int z) {
        int value = parentLayer.getInts(1, 1, x, z)[0];

        // Seules les zones océaniques peuvent devenir des océans profonds
        if (value != BiomeType.OCEAN.ordinal()) {
            return value;
        }

        // Vérifie les voisins pour voir si c'est un grand océan
        int oceanCount = 0;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                int neighborValue = parentLayer.getInts(1, 1, x + dx, z + dz)[0];
                if (neighborValue == BiomeType.OCEAN.ordinal()) {
                    oceanCount++;
                }
            }
        }

        // Si plus de 75% des voisins sont des océans, c'est un océan profond
        if (oceanCount >= 18) {
            return BiomeType.OCEAN.ordinal(); // Pourrait être un BiomeType.DEEP_OCEAN si disponible
        }

        return value;
    }
}
