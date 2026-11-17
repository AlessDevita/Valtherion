package fr.alessdevita.valtherion.world.gen.layer;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Couche de génération des biomes de base.
 * Crée la carte initiale des biomes à partir de valeurs aléatoires.
 */
public class GenLayerBiome extends GenLayer {

    private final int[][] biomeIds;

    /**
     * Crée une couche de biomes.
     * @param seed Graine pour la génération aléatoire
     */
    public GenLayerBiome(long seed) {
        super(seed);
        this.biomeIds = initBiomeIds();
    }

    /**
     * Initialise les IDs des biomes avec une distribution aléatoire.
     * @return Tableau 2D des IDs de biomes
     */
    private int[][] initBiomeIds() {
        int size = 256;
        int[][] ids = new int[size][size];

        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                // Distribution basée sur la position
                double lat = (z - size / 2) / (double) size;
                double rand = random.nextDouble();

                if (Math.abs(lat) > 0.8) {
                    // Zones polaires
                    ids[x][z] = BiomeType.SNOW.ordinal();
                } else if (Math.abs(lat) > 0.6) {
                    // Zones tempérées
                    if (rand < 0.4) {
                        ids[x][z] = BiomeType.FOREST.ordinal();
                    } else if (rand < 0.7) {
                        ids[x][z] = BiomeType.PLAINS.ordinal();
                    } else {
                        ids[x][z] = BiomeType.MOUNTAIN.ordinal();
                    }
                } else if (Math.abs(lat) > 0.3) {
                    // Zones tropicales
                    if (rand < 0.3) {
                        ids[x][z] = BiomeType.JUNGLE.ordinal();
                    } else if (rand < 0.6) {
                        ids[x][z] = BiomeType.FOREST.ordinal();
                    } else if (rand < 0.8) {
                        ids[x][z] = BiomeType.DESERT.ordinal();
                    } else {
                        ids[x][z] = BiomeType.PLAINS.ordinal();
                    }
                } else {
                    // Équateur
                    if (rand < 0.3) {
                        ids[x][z] = BiomeType.JUNGLE.ordinal();
                    } else if (rand < 0.6) {
                        ids[x][z] = BiomeType.SWAMP.ordinal();
                    } else {
                        ids[x][z] = BiomeType.PLAINS.ordinal();
                    }
                }
            }
        }
        return ids;
    }

    @Override
    protected int getValue(int x, int z) {
        // Mappe les coordonnées sur le tableau
        int size = biomeIds.length;
        int idx = ((x % size) + size) % size;
        int idz = ((z % size) + size) % size;
        return biomeIds[idx][idz];
    }
}
