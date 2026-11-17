package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche qui ajoute des îles dans les océans.
 */
public class GenLayerAddIsland extends GenLayer {

    private final int minIslandDistance;
    private final int islandSize;

    /**
     * Crée une couche d'ajout d'îles.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente des biomes
     * @param minIslandDistance Distance minimale entre les îles
     * @param islandSize Taille des îles ajoutées
     */
    public GenLayerAddIsland(long seed, GenLayer parentLayer, int minIslandDistance, int islandSize) {
        super(seed, parentLayer);
        this.minIslandDistance = minIslandDistance;
        this.islandSize = islandSize;
    }

    @Override
    protected int getValue(int x, int z) {
        int value = parentLayer.getInts(1, 1, x, z)[0];

        // N'ajoute des îles que dans les océans
        if (value != BiomeType.OCEAN.ordinal()) {
            return value;
        }

        // Vérifie si cette position est le centre d'une île
        if (isIslandCenter(x, z)) {
            // Vérifie la distance par rapport au centre de l'île
            int dx = x % minIslandDistance;
            int dz = z % minIslandDistance;
            if (dx < 0) dx += minIslandDistance;
            if (dz < 0) dz += minIslandDistance;

            int centerX = dx < minIslandDistance / 2 ? dx : dx - minIslandDistance;
            int centerZ = dz < minIslandDistance / 2 ? dz : dz - minIslandDistance;

            int distance = centerX * centerX + centerZ * centerZ;
            if (distance <= islandSize * islandSize) {
                return BiomeType.PLAINS.ordinal();
            }
        }

        return value;
    }

    /**
     * Vérifie si une position est le centre d'une île.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return true si c'est un centre d'île
     */
    private boolean isIslandCenter(int x, int z) {
        long hash = x * 374761393L + z * 668265263L;
        hash = hash * hash + hash;
        hash = hash ^ (hash >> 33);
        int rand = (int) (Math.abs(hash) % (minIslandDistance * minIslandDistance));

        return rand < minIslandDistance;
    }
}
