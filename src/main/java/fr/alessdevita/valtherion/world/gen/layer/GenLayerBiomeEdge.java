package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche de bordure de biomes qui crée des biomes de transition.
 * Par exemple, une forêt bordée par une plaine devient une forêt claire.
 */
public class GenLayerBiomeEdge extends GenLayer {

    private static final int[][] NEIGHBOR_OFFSETS = {
        {-1, -1}, {0, -1}, {1, -1},
        {-1,  0},           {1,  0},
        {-1,  1}, {0,  1}, {1,  1}
    };

    /**
     * Crée une couche de bordure de biomes.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente des biomes
     */
    public GenLayerBiomeEdge(long seed, GenLayer parentLayer) {
        super(seed, parentLayer);
    }

    @Override
    protected int getValue(int x, int z) {
        int centerValue = parentLayer.getInts(1, 1, x, z)[0];

        for (int[] offset : NEIGHBOR_OFFSETS) {
            int neighborValue = parentLayer.getInts(1, 1, x + offset[0], z + offset[1])[0];
            if (neighborValue != centerValue) {
                // Crée un biome de transition
                return getEdgeBiome(centerValue, neighborValue);
            }
        }

        return centerValue;
    }

    /**
     * Détermine le biome de transition entre deux biomes.
     * @param biome1 Premier biome
     * @param biome2 Deuxième biome
     * @return Le biome de transition
     */
    private int getEdgeBiome(int biome1, int biome2) {
        // Les forêts bordées par des plaines deviennent des forêts claires
        if ((biome1 == BiomeType.FOREST.ordinal() && biome2 == BiomeType.PLAINS.ordinal()) ||
            (biome1 == BiomeType.PLAINS.ordinal() && biome2 == BiomeType.FOREST.ordinal())) {
            return BiomeType.FOREST.ordinal();
        }

        // Les déserts bordés par des plaines deviennent des sablières
        if ((biome1 == BiomeType.DESERT.ordinal() && biome2 == BiomeType.PLAINS.ordinal()) ||
            (biome1 == BiomeType.PLAINS.ordinal() && biome2 == BiomeType.DESERT.ordinal())) {
            return BiomeType.DESERT.ordinal();
        }

        // Les montagnes bordées par n'importe quoi restent des montagnes
        if (biome1 == BiomeType.MOUNTAIN.ordinal() || biome2 == BiomeType.MOUNTAIN.ordinal()) {
            return BiomeType.MOUNTAIN.ordinal();
        }

        // Par défaut, garde le biome central
        return biome1;
    }
}
