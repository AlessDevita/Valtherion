package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui crée des biomes rares dans de petites zones isolées.
 */
public class GenLayerRareBiome extends GenLayer {

    private final int rareBiomeId;
    private final int minSize;
    private final int maxSize;

    /**
     * Crée une couche de biomes rares.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente des biomes
     * @param rareBiomeId ID du biome rare à placer
     * @param minSize Taille minimale d'une zone rare
     * @param maxSize Taille maximale d'une zone rare
     */
    public GenLayerRareBiome(long seed, GenLayer parentLayer, int rareBiomeId, int minSize, int maxSize) {
        super(seed, parentLayer);
        this.rareBiomeId = rareBiomeId;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    protected int getValue(int x, int z) {
        int value = parentLayer.getInts(1, 1, x, z)[0];

        // Vérifie si cette position est le centre d'une zone rare
        if (isRareBiomeCenter(x, z)) {
            return rareBiomeId;
        }

        // Vérifie si cette position est dans une zone rare voisine
        for (int dx = -maxSize; dx <= maxSize; dx++) {
            for (int dz = -maxSize; dz <= maxSize; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (isRareBiomeCenter(x + dx, z + dz)) {
                    int distance = Math.abs(dx) + Math.abs(dz);
                    if (distance <= random.nextInt(maxSize)) {
                        return rareBiomeId;
                    }
                }
            }
        }

        return value;
    }

    /**
     * Vérifie si une position est le centre d'une zone de biome rare.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return true si c'est un centre de biome rare
     */
    private boolean isRareBiomeCenter(int x, int z) {
        // Utilise un hash déterministe pour la position
        long hash = x * 374761393L + z * 668265263L;
        hash = hash * hash + hash;
        hash = hash ^ (hash >> 33);
        int rand = (int) (Math.abs(hash) % 10000);

        // 1% de chance d'être un centre de biome rare
        return rand < 100;
    }
}
