package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui définit le tracé des rivières.
 */
public class GenLayerRiver extends GenLayer {

    /**
     * Crée une couche de rivières.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente
     */
    public GenLayerRiver(long seed, GenLayer parentLayer) {
        super(seed, parentLayer);
    }

    @Override
    protected int getValue(int x, int z) {
        int value = parentLayer.getInts(1, 1, x, z)[0];

        // Si c'est déjà une rivière, garde la rivière
        if (value == BiomeType.RIVER.ordinal()) {
            return value;
        }

        // Vérifie les voisins pour voir si une rivière passe près
        int[] neighbors = parentLayer.getInts(3, 3, x - 1, z - 1);
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i] == BiomeType.RIVER.ordinal()) {
                // Étend la rivière légèrement
                long hash = x * 3L + z * 7L + i;
                int rand = (int) (Math.abs(hash) % 100);
                if (rand < 20) {
                    return BiomeType.RIVER.ordinal();
                }
            }
        }

        return value;
    }
}
