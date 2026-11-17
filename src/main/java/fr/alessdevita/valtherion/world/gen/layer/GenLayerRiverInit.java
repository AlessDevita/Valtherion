package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui initialise les rivières dans les zones de plaines.
 */
public class GenLayerRiverInit extends GenLayer {

    /**
     * Crée une couche d'initialisation des rivières.
     * @param seed Graine pour la génération aléatoire
     */
    public GenLayerRiverInit(long seed) {
        super(seed);
    }

    @Override
    protected int getValue(int x, int z) {
        // Crée un réseau de rivières basé sur un hash
        long hash = x * 374761393L + z * 668265263L;
        hash = hash * hash + hash;
        hash = hash ^ (hash >> 33);
        int rand = (int) (Math.abs(hash) % 100);

        // 5% de chance d'être une rivière
        if (rand < 5) {
            return BiomeType.RIVER.ordinal();
        }

        // 0 par défaut (pas de rivière)
        return 0;
    }
}
