package fr.alessdevita.valtherion.world.chunk;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.world.gen.NoiseGeneratorOctaves;
import fr.alessdevita.valtherion.world.gen.layer.GenLayer;
import fr.alessdevita.valtherion.world.gen.layer.GenLayerBiome;
import fr.alessdevita.valtherion.world.gen.layer.GenLayerEdge;
import fr.alessdevita.valtherion.world.gen.layer.GenLayerVoronoiZoom;
import fr.alessdevita.valtherion.world.gen.layer.GenLayerZoom;

import java.util.*;

/**
 * Gère la génération procédurale du terrain pour un chunk.
 * Utilise du bruit Perlin multi-octaves, des couches de biomes, et génère
 * un monde aléatoire style Minecraft avec terrain, grottes, minerais, eau, lave et décorations.
 */
public class ChunkGenerator {
    private final long seed;
    private final Random random;

    // Générateurs de bruit pour le terrain
    private final NoiseGeneratorOctaves heightNoise1;
    private final NoiseGeneratorOctaves heightNoise2;
    private final NoiseGeneratorOctaves heightNoise3;
    private final NoiseGeneratorOctaves caveNoise;

    // Couches de biomes
    private final GenLayer biomeLayer;

    // Niveaux d'eau et de lave
    private static final int WATER_LEVEL = 64;
    private static final int LAVA_LEVEL = 10;

    // Probabilités de minerais
    private static final float COAL_ORE_CHANCE = 0.05f;
    private static final float IRON_ORE_CHANCE = 0.03f;
    private static final float GOLD_ORE_CHANCE = 0.01f;
    private static final float DIAMOND_ORE_CHANCE = 0.005f;

    /**
     * Crée un générateur de chunk avec une graine.
     * @param seed Graine pour la génération procédurale
     */
    public ChunkGenerator(long seed) {
        this.seed = seed;
        this.random = new Random(seed);

        // Initialiser les générateurs de bruit multi-octaves
        Random heightRand1 = new Random(seed ^ 0x1234567890L);
        Random heightRand2 = new Random(seed ^ 0x9876543210L);
        Random heightRand3 = new Random(seed ^ 0xABCDEF0123L);
        Random caveRand = new Random(seed ^ 0xFEDCBA9876L);

        this.heightNoise1 = new NoiseGeneratorOctaves(heightRand1, 8, 0.5);
        this.heightNoise2 = new NoiseGeneratorOctaves(heightRand2, 4, 0.5);
        this.heightNoise3 = new NoiseGeneratorOctaves(heightRand3, 4, 0.5);
        this.caveNoise = new NoiseGeneratorOctaves(caveRand, 3, 0.4);

        // Construire la chaîne de couches de biomes
        GenLayer biomeBase = new GenLayerBiome(seed + 1L);
        GenLayer zoom1 = new GenLayerZoom(seed + 2L, biomeBase, 1);
        GenLayer zoom2 = new GenLayerZoom(seed + 3L, zoom1, 1);
        GenLayer voronoi = new GenLayerVoronoiZoom(seed + 4L, zoom2, 1);
        this.biomeLayer = new GenLayerEdge(seed + 5L, voronoi, new int[0]);
    }

    /**
     * Génère le contenu d'un chunk.
     * @param chunk Chunk à générer
     */
    public void generate(Chunk chunk) {
        generateTerrain(chunk);
        generateCaves(chunk);
        generateOres(chunk);
        generateWaterAndLava(chunk);
        generateDecorations(chunk);
    }

    /**
     * Génère le terrain du chunk en utilisant du bruit Perlin multi-octaves.
     * @param chunk Chunk à générer
     */
    private void generateTerrain(Chunk chunk) {
        int chunkX = chunk.getChunkX() * Chunk.SIZE;
        int chunkZ = chunk.getChunkZ() * Chunk.SIZE;

        // Pré-calculer les hauteurs pour le chunk
        int[][] heights = new int[Chunk.SIZE][Chunk.SIZE];

        for (int x = 0; x < Chunk.SIZE; x++) {
            for (int z = 0; z < Chunk.SIZE; z++) {
                int worldX = chunkX + x;
                int worldZ = chunkZ + z;

                // Combinaison de plusieurs bruits pour un terrain naturel
                double n1 = heightNoise1.noise(worldX * 0.005, worldZ * 0.005);
                double n2 = heightNoise2.noise(worldX * 0.02, worldZ * 0.02);
                double n3 = heightNoise3.noise(worldX * 0.05, worldZ * 0.05);

                double combined = (n1 * 0.6 + n2 * 0.3 + n3 * 0.1) * 64.0;

                // Déterminer le biome pour ajuster la hauteur
                BiomeType biome = getBiome(worldX, worldZ);
                double biomeMultiplier = getBiomeHeightMultiplier(biome);

                int height = (int)(64 + combined * biomeMultiplier);

                // Contraintes de hauteur
                if (biome == BiomeType.OCEAN) {
                    height = Math.min(height, WATER_LEVEL - 4);
                }
                height = Math.max(1, Math.min(height, Chunk.HEIGHT - 10));

                heights[x][z] = height;

                // Remplir la colonne de blocs
                for (int y = 0; y < Chunk.HEIGHT; y++) {
                    if (y < height - 4) {
                        // Pierre profonde
                        chunk.setBlock(x, y, z, createBlock(BlockType.STONE));
                    } else if (y < height - 1) {
                        // Terre sous la surface
                        if (biome == BiomeType.DESERT) {
                            chunk.setBlock(x, y, z, createBlock(BlockType.SAND));
                        } else if (biome == BiomeType.SNOW) {
                            chunk.setBlock(x, y, z, createBlock(BlockType.STONE));
                        } else {
                            chunk.setBlock(x, y, z, createBlock(BlockType.DIRT));
                        }
                    } else if (y == height - 1) {
                        // Bloc de surface selon le biome
                        chunk.setBlock(x, y, z, createBlock(getSurfaceBlock(biome)));
                    } else {
                        // Air
                        chunk.setBlock(x, y, z, createBlock(BlockType.AIR));
                    }
                }
            }
        }
    }

    /**
     * Génère des grottes souterraines en utilisant du bruit 3D.
     * @param chunk Chunk à creuser
     */
    private void generateCaves(Chunk chunk) {
        int chunkX = chunk.getChunkX() * Chunk.SIZE;
        int chunkZ = chunk.getChunkZ() * Chunk.SIZE;

        for (int x = 0; x < Chunk.SIZE; x++) {
            for (int z = 0; z < Chunk.SIZE; z++) {
                for (int y = 0; y < 60; y++) {
                    int worldX = chunkX + x;
                    int worldZ = chunkZ + z;

                    // Bruit 3D pour les grottes
                    double cave = caveNoise.noise(
                        worldX * 0.05,
                        y * 0.08,
                        worldZ * 0.05
                    );

                    // Si le bruit est au-dessus d'un seuil, creuser
                    if (cave > 0.6) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block != null && block.getType() != BlockType.AIR &&
                            block.getType() != BlockType.WATER &&
                            block.getType() != BlockType.LAVA) {
                            chunk.setBlock(x, y, z, createBlock(BlockType.AIR));
                        }
                    }
                }
            }
        }
    }

    /**
     * Génère des minerais dans le chunk.
     * @param chunk Chunk à miner
     */
    private void generateOres(Chunk chunk) {
        for (int x = 0; x < Chunk.SIZE; x++) {
            for (int z = 0; z < Chunk.SIZE; z++) {
                for (int y = 0; y < Chunk.HEIGHT; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block == null || block.getType() != BlockType.STONE) {
                        continue;
                    }

                    // Utiliser un hash basé sur la position pour la randomisation
                    long hash = seed ^ ((long)x << 32) ^ ((long)z << 16) ^ (long)y;
                    Random oreRandom = new Random(hash);

                    // Charbon - courant, trouvé haut
                    if (y < 80 && oreRandom.nextFloat() < COAL_ORE_CHANCE) {
                        chunk.setBlock(x, y, z, createBlock(BlockType.COBBLESTONE));
                        continue;
                    }

                    // Fer - moins courant, trouvé au milieu
                    if (y < 60 && y > 16 && oreRandom.nextFloat() < IRON_ORE_CHANCE) {
                        chunk.setBlock(x, y, z, createBlock(BlockType.COBBLESTONE));
                        continue;
                    }

                    // Or - rare, trouvé bas
                    if (y < 32 && oreRandom.nextFloat() < GOLD_ORE_CHANCE) {
                        chunk.setBlock(x, y, z, createBlock(BlockType.GOLD_ORE));
                        continue;
                    }

                    // Diamant - très rare, trouvé très bas
                    if (y < 16 && oreRandom.nextFloat() < DIAMOND_ORE_CHANCE) {
                        chunk.setBlock(x, y, z, createBlock(BlockType.COBBLESTONE));
                    }
                }
            }
        }
    }

    /**
     * Remplit les zones basses avec de l'eau et de la lave.
     * @param chunk Chunk à remplir
     */
    private void generateWaterAndLava(Chunk chunk) {
        for (int x = 0; x < Chunk.SIZE; x++) {
            for (int z = 0; z < Chunk.SIZE; z++) {
                // Trouver la hauteur du terrain
                int terrainHeight = 0;
                for (int y = Chunk.HEIGHT - 1; y >= 0; y--) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block != null && block.getType() != BlockType.AIR) {
                        terrainHeight = y;
                        break;
                    }
                }

                // Remplir avec de l'eau si le terrain est en dessous du niveau de l'eau
                for (int y = (int)terrainHeight + 1; y < WATER_LEVEL; y++) {
                    chunk.setBlock(x, y, z, createBlock(BlockType.WATER));
                }

                // Remplir avec de la lave très bas
                for (int y = 0; y < LAVA_LEVEL; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block != null && block.getType() == BlockType.AIR) {
                        chunk.setBlock(x, y, z, createBlock(BlockType.LAVA));
                    }
                }
            }
        }
    }

    /**
     * Génère les décorations (arbres, fleurs, cactus, etc.) selon le biome.
     * @param chunk Chunk à décorer
     */
    private void generateDecorations(Chunk chunk) {
        for (int x = 0; x < Chunk.SIZE; x++) {
            for (int z = 0; z < Chunk.SIZE; z++) {
                // Trouver la hauteur du terrain
                int height = getTerrainHeight(chunk, x, z);
                if (height <= 0) continue;

                Block surfaceBlock = chunk.getBlock(x, height - 1, z);
                if (surfaceBlock == null) continue;

                BiomeType biome = getBiome(
                    chunk.getChunkX() * Chunk.SIZE + x,
                    chunk.getChunkZ() * Chunk.SIZE + z
                );

                // Décorations selon le biome
                switch (biome) {
                    case FOREST:
                        // Arbres fréquents dans les forêts (8%)
                        if (random.nextDouble() < 0.08) {
                            generateTree(chunk, x, height, z, 5 + random.nextInt(3));
                        }
                        break;
                    case JUNGLE:
                        // Arbres très fréquents dans la jungle (15%)
                        if (random.nextDouble() < 0.15) {
                            generateTree(chunk, x, height, z, 6 + random.nextInt(4));
                        }
                        break;
                    case PLAINS:
                        // Arbres rares dans les plaines (1%)
                        if (random.nextDouble() < 0.01) {
                            generateTree(chunk, x, height, z, 4 + random.nextInt(2));
                        }
                        // Fleurs fréquentes (3%)
                        if (random.nextDouble() < 0.03) {
                            generateFlower(chunk, x, height, z);
                        }
                        break;
                    case DESERT:
                        // Cactus rares dans le désert (0.5%)
                        if (random.nextDouble() < 0.005) {
                            generateCactus(chunk, x, height, z);
                        }
                        break;
                    case SNOW:
                        // Neige sur la surface
                        if (surfaceBlock.getType() == BlockType.GRASS) {
                            chunk.setBlock(x, height - 1, z, createBlock(BlockType.SNOW));
                        }
                        break;
                    case SWAMP:
                        // Arbres et champignons dans les marais
                        if (random.nextDouble() < 0.02) {
                            generateTree(chunk, x, height, z, 4 + random.nextInt(2));
                        }
                        if (random.nextDouble() < 0.01) {
                            generateMushroom(chunk, x, height, z);
                        }
                        break;
                    case MOUNTAIN:
                        // Arbres rares en montagne
                        if (random.nextDouble() < 0.005 && height < 100) {
                            generateTree(chunk, x, height, z, 4 + random.nextInt(2));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Génère un arbre à une position donnée.
     * @param chunk Chunk où placer l'arbre
     * @param x Coordonnée X locale
     * @param y Coordonnée Y locale
     * @param z Coordonnée Z locale
     * @param height Hauteur du tronc
     */
    private void generateTree(Chunk chunk, int x, int y, int z, int height) {
        // Tronc
        for (int i = 0; i < height; i++) {
            if (y + i < Chunk.HEIGHT) {
                chunk.setBlock(x, y + i, z, createBlock(BlockType.WOOD));
            }
        }

        // Feuillage en dôme
        int leafStart = y + height - 2;
        int leafEnd = y + height + 1;
        for (int ly = leafStart; ly <= leafEnd; ly++) {
            if (ly >= Chunk.HEIGHT) break;
            int radius = (ly == leafEnd) ? 1 : 2;
            for (int lx = -radius; lx <= radius; lx++) {
                for (int lz = -radius; lz <= radius; lz++) {
                    int wx = x + lx;
                    int wz = z + lz;
                    if (wx >= 0 && wx < Chunk.SIZE && wz >= 0 && wz < Chunk.SIZE) {
                        if (!(lx == 0 && lz == 0 && ly < y + height)) {
                            chunk.setBlock(wx, ly, wz, createBlock(BlockType.LEAVES));
                        }
                    }
                }
            }
        }
    }

    /**
     * Génère une fleur à une position donnée.
     * @param chunk Chunk où placer la fleur
     * @param x Coordonnée X locale
     * @param y Coordonnée Y locale
     * @param z Coordonnée Z locale
     */
    private void generateFlower(Chunk chunk, int x, int y, int z) {
        BlockType flowerType;
        switch (random.nextInt(3)) {
            case 0: flowerType = BlockType.FLOWER_RED; break;
            case 1: flowerType = BlockType.FLOWER_YELLOW; break;
            default: flowerType = BlockType.FLOWER_BLUE; break;
        }
        chunk.setBlock(x, y, z, createBlock(flowerType));
    }

    /**
     * Génère un cactus à une position donnée.
     * @param chunk Chunk où placer le cactus
     * @param x Coordonnée X locale
     * @param y Coordonnée Y locale
     * @param z Coordonnée Z locale
     */
    private void generateCactus(Chunk chunk, int x, int y, int z) {
        int cactusHeight = 2 + random.nextInt(2);
        for (int i = 0; i < cactusHeight; i++) {
            if (y + i < Chunk.HEIGHT) {
                chunk.setBlock(x, y + i, z, createBlock(BlockType.CACTUS));
            }
        }
    }

    /**
     * Génère un champignon à une position donnée.
     * @param chunk Chunk où placer le champignon
     * @param x Coordonnée X locale
     * @param y Coordonnée Y locale
     * @param z Coordonnée Z locale
     */
    private void generateMushroom(Chunk chunk, int x, int y, int z) {
        BlockType mushroomType = random.nextBoolean() ? BlockType.MUSHROOM_RED : BlockType.MUSHROOM_BROWN;
        chunk.setBlock(x, y, z, createBlock(mushroomType));
    }

    /**
     * Récupère le biome à une position donnée en utilisant les couches GenLayer.
     * @param x Coordonnée X mondiale
     * @param z Coordonnée Z mondiale
     * @return Type de biome
     */
    private BiomeType getBiome(int x, int z) {
        int[] biomes = biomeLayer.getInts(1, 1, x >> 2, z >> 2);
        int biomeId = biomes[0] % BiomeType.values().length;
        if (biomeId < 0) biomeId += BiomeType.values().length;
        return BiomeType.values()[biomeId];
    }

    /**
     * Retourne le multiplicateur de hauteur pour un biome.
     * @param biome Type de biome
     * @return Multiplicateur de hauteur
     */
    private double getBiomeHeightMultiplier(BiomeType biome) {
        switch (biome) {
            case MOUNTAIN: return 2.5;
            case FOREST: return 1.2;
            case JUNGLE: return 1.1;
            case PLAINS: return 0.8;
            case DESERT: return 0.7;
            case OCEAN: return 0.3;
            case RIVER: return 0.4;
            case SWAMP: return 0.5;
            case SNOW: return 1.5;
            default: return 1.0;
        }
    }

    /**
     * Retourne le bloc de surface pour un biome.
     * @param biome Type de biome
     * @return Type de bloc de surface
     */
    private BlockType getSurfaceBlock(BiomeType biome) {
        switch (biome) {
            case DESERT: return BlockType.SAND;
            case SNOW: return BlockType.SNOW;
            case SWAMP: return BlockType.GRASS;
            case OCEAN: return BlockType.SAND;
            case NETHER: return BlockType.NETHER_RACK;
            default: return BlockType.GRASS;
        }
    }

    /**
     * Trouve la hauteur du terrain à une position donnée.
     * @param chunk Chunk
     * @param x Coordonnée X locale
     * @param z Coordonnée Z locale
     * @return Hauteur du terrain ou 0 si non trouvé
     */
    private int getTerrainHeight(Chunk chunk, int x, int z) {
        for (int y = Chunk.HEIGHT - 1; y >= 0; y--) {
            Block block = chunk.getBlock(x, y, z);
            if (block != null && block.getType() != BlockType.AIR &&
                block.getType() != BlockType.WATER) {
                return y + 1;
            }
        }
        return 0;
    }

    /**
     * Crée une instance de bloc.
     * @param type Type de bloc à créer
     * @return Instance de Block
     */
    private Block createBlock(BlockType type) {
        return new Block(type) {
            @Override
            public void update(float deltaTime) {
            }
        };
    }
}