package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui convertit les plaines en montagnes dans certaines zones.
 */
public class GenLayerHills extends GenLayer {

    /**
     * Crée une couche de montagnes.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente des biomes
     */
    public GenLayerHills(long seed, GenLayer parentLayer) {
        super(seed, parentLayer);
    }

    @Override
    protected int getValue(int x, int z) {
        int value = parentLayer.getInts(1, 1, x, z)[0];

        // Les plaines peuvent devenir des montagnes
        if (value == BiomeType.PLAINS.ordinal()) {
            long hash = x * 3L + z * 7L;
            hash = hash * hash + hash;
            hash = hash ^ (hash >> 33);
            int rand = (int) (Math.abs(hash) % 100);

            if (rand < 15) {
                return BiomeType.MOUNTAIN.ordinal();
            }
        }

        // Les forêts peuvent devenir des forêts de montagnes
        if (value == BiomeType.FOREST.ordinal()) {
            long hash = x * 5L + z * 11L;
            hash = hash * hash + hash;
            hash = hash ^ (hash >> 33);
            int rand = (int) (Math.abs(hash) % 100);

            if (rand < 10) {
                return BiomeType.MOUNTAIN.ordinal();
            }
        }

        return value;
    }
}
