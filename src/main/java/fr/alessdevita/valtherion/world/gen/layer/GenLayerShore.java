package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui crée des plages à la frontière entre terre et mer.
 */
public class GenLayerShore extends GenLayer {

    private static final int[][] NEIGHBOR_OFFSETS = {
        {-1, -1}, {0, -1}, {1, -1},
        {-1,  0},           {1,  0},
        {-1,  1}, {0,  1}, {1,  1}
    };

    /**
     * Crée une couche de plages.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente des biomes
     */
    public GenLayerShore(long seed, GenLayer parentLayer) {
        super(seed, parentLayer);
    }

    @Override
    protected int getValue(int x, int z) {
        int value = parentLayer.getInts(1, 1, x, z)[0];

        // Seules les zones océaniques peuvent devenir des plages
        if (value != BiomeType.OCEAN.ordinal()) {
            return value;
        }

        // Vérifie les voisins pour voir s'il y a de la terre
        for (int[] offset : NEIGHBOR_OFFSETS) {
            int neighborValue = parentLayer.getInts(1, 1, x + offset[0], z + offset[1])[0];
            if (neighborValue != BiomeType.OCEAN.ordinal() && neighborValue != 0) {
                // C'est une plage
                return BiomeType.DESERT.ordinal(); // Le sable est représenté par DESERT ici
            }
        }

        return value;
    }
}
