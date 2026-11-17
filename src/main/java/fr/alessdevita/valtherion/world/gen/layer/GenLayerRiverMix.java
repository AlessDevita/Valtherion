package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui mélange les rivières avec les biomes.
 * Les rivières remplacent les biomes terrestres mais pas les océans.
 */
public class GenLayerRiverMix extends GenLayer {

    /**
     * Crée une couche de mélange des rivières.
     * @param seed Graine pour la génération aléatoire
     * @param biomeLayer Couche des biomes
     * @param riverLayer Couche des rivières
     */
    public GenLayerRiverMix(long seed, GenLayer biomeLayer, GenLayer riverLayer) {
        super(seed, biomeLayer);
    }

    @Override
    protected int getValue(int x, int z) {
        int biomeValue = parentLayer.getInts(1, 1, x, z)[0];

        // Les rivières ne remplacent pas les océans
        if (biomeValue == BiomeType.OCEAN.ordinal()) {
            return biomeValue;
        }

        // Vérifie s'il y a une rivière à cette position
        long hash = x * 374761393L + z * 668265263L;
        hash = hash * hash + hash;
        hash = hash ^ (hash >> 33);
        int rand = (int) (Math.abs(hash) % 100);

        // 3% de chance d'avoir une rivière
        if (rand < 3) {
            return BiomeType.RIVER.ordinal();
        }

        return biomeValue;
    }
}
