package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui ajoute de la neige dans les zones froides.
 */
public class GenLayerAddSnow extends GenLayer {

    /**
     * Crée une couche d'ajout de neige.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente des biomes
     */
    public GenLayerAddSnow(long seed, GenLayer parentLayer) {
        super(seed, parentLayer);
    }

    @Override
    protected int getValue(int x, int z) {
        int value = parentLayer.getInts(1, 1, x, z)[0];

        // Les zones de montagne et les zones polaires reçoivent de la neige
        if (value == BiomeType.MOUNTAIN.ordinal() || value == BiomeType.SNOW.ordinal()) {
            return value;
        }

        // Vérifie la latitude pour ajouter de la neige aux pôles
        long hash = z * 374761393L;
        hash = hash * hash + hash;
        hash = hash ^ (hash >> 33);
        int latitude = (int) (Math.abs(hash) % 100);

        if (latitude > 85) {
            // Zone polaire, convertit en neige
            return BiomeType.SNOW.ordinal();
        }

        return value;
    }
}
