package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.world.chunk.Chunk;
import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.entity.living.Player;

import java.util.*;

/**
 * Classe pour générer le monde dans Valtherion.
 * Gère la génération procédurale des chunks, des biomes, et des structures.
 */
public class WorldGenerator {

    // Monde associé au générateur
    private final World world;

    // Graine pour la génération aléatoire
    private long seed;

    // Générateur de nombres aléatoires
    private Random random;

    // Taille d'un chunk (en blocs)
    private int chunkSize;

    // Hauteur maximale du terrain
    private int maxHeight;

    // Liste des biomes disponibles
    private final Map<BiomeType, Biome> biomes;

    // Probabilité de génération des biomes
    private final Map<BiomeType, Float> biomeProbabilities;

    // Liste des structures à générer
    private final List<Structure> structures;

    // Probabilité de génération des structures
    private float structureProbability;

    // Classe pour représenter une structure
    public static class Structure {
        private final String name;
        private final BlockType[][][] blocks;
        private final int width;
        private final int height;
        private final int depth;

        public Structure(String name, BlockType[][][] blocks) {
            this.name = name;
            this.blocks = blocks;
            this.depth = blocks.length;
            this.height = blocks[0].length;
            this.width = blocks[0][0].length;
        }

        public String getName() {
            return name;
        }

        public BlockType[][][] getBlocks() {
            return blocks;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getDepth() {
            return depth;
        }
    }

    /**
     * Constructeur du générateur de monde.
     * @param world Monde associé.
     */
    public WorldGenerator(World world) {
        this.world = world;
        this.seed = System.currentTimeMillis();
        this.random = new Random(seed);
        this.chunkSize = world.getChunkSize();
        this.maxHeight = 128;
        this.biomes = new HashMap<>();
        this.biomeProbabilities = new HashMap<>();
        this.structures = new ArrayList<>();
        this.structureProbability = 0.01f; // 1% de chance de générer une structure par chunk

        // Initialiser les biomes par défaut
        initDefaultBiomes();

        // Initialiser les structures par défaut
        initDefaultStructures();
    }

    /**
     * Constructeur du générateur de monde avec une graine.
     * @param world Monde associé.
     * @param seed Graine pour la génération aléatoire.
     */
    public WorldGenerator(World world, long seed) {
        this(world);
        this.seed = seed;
        this.random = new Random(seed);
    }

    /**
     * Initialise les biomes par défaut.
     */
    private void initDefaultBiomes() {
        // Biome de plaine
        Biome plainsBiome = new Biome(BiomeType.PLAINS, "Plaine", "Un biome de plaine.");
        plainsBiome.setBaseHeight(64);
        plainsBiome.setHeightVariation(5);
        plainsBiome.addBlock(BlockType.GRASS, 0.8f);
        plainsBiome.addBlock(BlockType.DIRT, 0.15f);
        plainsBiome.addBlock(BlockType.STONE, 0.05f);
        biomes.put(BiomeType.PLAINS, plainsBiome);
        biomeProbabilities.put(BiomeType.PLAINS, 0.5f);

        // Biome de forêt
        Biome forestBiome = new Biome(BiomeType.FOREST, "Forêt", "Un biome de forêt.");
        forestBiome.setBaseHeight(70);
        forestBiome.setHeightVariation(10);
        forestBiome.addBlock(BlockType.GRASS, 0.6f);
        forestBiome.addBlock(BlockType.DIRT, 0.2f);
        forestBiome.addBlock(BlockType.WOOD, 0.1f);
        forestBiome.addBlock(BlockType.LEAVES, 0.05f);
        forestBiome.addBlock(BlockType.STONE, 0.05f);
        biomes.put(BiomeType.FOREST, forestBiome);
        biomeProbabilities.put(BiomeType.FOREST, 0.3f);

        // Biome de montagne
        Biome mountainBiome = new Biome(BiomeType.MOUNTAIN, "Montagne", "Un biome de montagne.");
        mountainBiome.setBaseHeight(96);
        mountainBiome.setHeightVariation(20);
        mountainBiome.addBlock(BlockType.STONE, 0.7f);
        mountainBiome.addBlock(BlockType.DIRT, 0.2f);
        mountainBiome.addBlock(BlockType.GRASS, 0.1f);
        biomes.put(BiomeType.MOUNTAIN, mountainBiome);
        biomeProbabilities.put(BiomeType.MOUNTAIN, 0.1f);

        // Biome de désert
        Biome desertBiome = new Biome(BiomeType.DESERT, "Désert", "Un biome de désert.");
        desertBiome.setBaseHeight(60);
        desertBiome.setHeightVariation(3);
        desertBiome.addBlock(BlockType.SAND, 0.9f);
        desertBiome.addBlock(BlockType.STONE, 0.1f);
        biomes.put(BiomeType.DESERT, desertBiome);
        biomeProbabilities.put(BiomeType.DESERT, 0.1f);
    }

    /**
     * Initialise les structures par défaut.
     */
    private void initDefaultStructures() {
        // Structure : Arbre
        BlockType[][][] treeStructure = new BlockType[3][5][3];

        // Tronc de l'arbre
        for (int y = 0; y < 4; y++) {
            treeStructure[1][y][1] = BlockType.WOOD;
        }

        // Feuilles de l'arbre
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (x == 1 && z == 1) {
                    treeStructure[x][4][z] = BlockType.LEAVES;
                } else {
                    treeStructure[x][4][z] = BlockType.LEAVES;
                }
            }
        }

        structures.add(new Structure("Arbre", treeStructure));

        // Structure : Maison
        BlockType[][][] houseStructure = new BlockType[5][4][5];

        // Murs de la maison
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                if (x == 0 || x == 4 || z == 0 || z == 4) {
                    for (int y = 0; y < 3; y++) {
                        houseStructure[x][y][z] = BlockType.WOOD;
                    }
                }
            }
        }

        // Toit de la maison
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                if (x == 0 || x == 4 || z == 0 || z == 4) {
                    houseStructure[x][3][z] = BlockType.WOOD;
                } else {
                    houseStructure[x][3][z] = BlockType.WOOD;
                }
            }
        }

        structures.add(new Structure("Maison", houseStructure));
    }

    /**
     * Génère le monde initial.
     */
    public void generateInitialWorld() {
        // Générer les chunks initiaux autour de l'origine
        int renderDistance = 5; // Distance de rendu en chunks
        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {
                generateChunk(x, z);
            }
        }

        // Générer le joueur initial
        generateInitialPlayer();

        System.out.println("Monde initial généré.");
    }

    /**
     * Génère un chunk.
     * @param chunkX Coordonnée X du chunk.
     * @param chunkZ Coordonnée Z du chunk.
     */
    public void generateChunk(int chunkX, int chunkZ) {
        Chunk chunk = world.getChunk(chunkX, chunkZ);
        if (chunk != null) {
            return; // Le chunk existe déjà
        }

        // Créer un nouveau chunk
        chunk = new Chunk(chunkX, chunkZ, chunkSize);

        // Générer le terrain pour le chunk
        generateTerrain(chunk);

        // Générer les structures pour le chunk
        generateStructures(chunk);

        // Ajouter le chunk au monde
        world.addChunk(chunk);
    }

    /**
     * Génère le terrain pour un chunk.
     * @param chunk Chunk à générer.
     */
    private void generateTerrain(Chunk chunk) {
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        // Déterminer le biome pour ce chunk
        Biome biome = getBiome(chunkX, chunkZ);

        // Générer le terrain en fonction du biome
        for (int x = 0; x < chunkSize; x++) {
            for (int z = 0; z < chunkSize; z++) {
                int worldX = chunkX * chunkSize + x;
                int worldZ = chunkZ * chunkSize + z;

                // Générer la hauteur du terrain
                int height = generateHeight(worldX, worldZ, biome);

                // Générer les blocs pour cette colonne
                for (int y = 0; y < maxHeight; y++) {
                    if (y < height) {
                        BlockType blockType = biome.getRandomBlock(random);
                        chunk.setBlock(x, y, z, new Block(blockType) {
                            @Override
                            public void update(float deltaTime) {

                            }
                        });
                    } else if (y == height) {
                        // Bloc de surface (ex: herbe, sable)
                        BlockType surfaceBlock = biome.getSurfaceBlock();
                        chunk.setBlock(x, y, z, new Block(surfaceBlock) {
                            @Override
                            public void update(float deltaTime) {

                            }
                        });
                    } else {
                        // Air
                        chunk.setBlock(x, y, z, new Block(BlockType.AIR) {
                            @Override
                            public void update(float deltaTime) {

                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * Génère la hauteur du terrain pour une position donnée.
     * @param x Coordonnée X.
     * @param z Coordonnée Z.
     * @param biome Biome pour cette position.
     * @return Hauteur du terrain.
     */
    private int generateHeight(int x, int z, Biome biome) {
        // Utiliser le bruit de Perlin pour générer la hauteur
        float noise = generatePerlinNoise(x * 0.1f, z * 0.1f);

        // Calculer la hauteur en fonction du biome et du bruit
        int baseHeight = biome.getBaseHeight();
        int heightVariation = (int) biome.getHeightVariation();

        return baseHeight + (int) (noise * heightVariation);
    }

    /**
     * Génère un bruit de Perlin pour une position donnée.
     * @param x Coordonnée X.
     * @param z Coordonnée Z.
     * @return Valeur du bruit de Perlin.
     */
    private float generatePerlinNoise(float x, float z) {
        // Implémentation simplifiée du bruit de Perlin
        // Pour une implémentation complète, utilise une bibliothèque comme FastNoise ou JOML Noise
        return random.nextFloat() * 2 - 1; // Valeur aléatoire entre -1 et 1
    }

    /**
     * Génère les structures pour un chunk.
     * @param chunk Chunk à générer.
     */
    private void generateStructures(Chunk chunk) {
        if (random.nextFloat() > structureProbability) {
            return; // Pas de structure pour ce chunk
        }

        // Choisir une structure aléatoire
        Structure structure = structures.get(random.nextInt(structures.size()));

        // Choisir une position aléatoire dans le chunk
        int x = random.nextInt(chunkSize - structure.getWidth());
        int z = random.nextInt(chunkSize - structure.getDepth());

        // Générer la structure
        generateStructure(chunk, structure, x, 0, z);
    }

    /**
     * Génère une structure dans un chunk.
     * @param chunk Chunk où générer la structure.
     * @param structure Structure à générer.
     * @param x Coordonnée X de la structure.
     * @param y Coordonnée Y de la structure.
     * @param z Coordonnée Z de la structure.
     */
    private void generateStructure(Chunk chunk, Structure structure, int x, int y, int z) {
        BlockType[][][] blocks = structure.getBlocks();

        for (int structureX = 0; structureX < structure.getWidth(); structureX++) {
            for (int structureY = 0; structureY < structure.getHeight(); structureY++) {
                for (int structureZ = 0; structureZ < structure.getDepth(); structureZ++) {
                    BlockType blockType = blocks[structureX][structureY][structureZ];
                    if (blockType != null) {
                        chunk.setBlock(
                                x + structureX,
                                y + structureY,
                                z + structureZ,
                                new Block(blockType) {
                                    @Override
                                    public void update(float deltaTime) {

                                    }
                                }
                        );
                    }
                }
            }
        }
    }

    /**
     * Génère le joueur initial.
     */
    private void generateInitialPlayer() {
        // Trouver une position de spawn valide
        int spawnX = 0;
        int spawnY = 64;
        int spawnZ = 0;

        // Vérifier si la position de spawn est valide
        while (world.isBlockSolid(spawnX, spawnY, spawnZ)) {
            spawnY++;
        }

        // Créer le joueur
        Player player = new Player(world, spawnX, spawnY, spawnZ, "Alessandro");
        world.addPlayer(player);

        System.out.println("Joueur initial généré à : (" + spawnX + ", " + spawnY + ", " + spawnZ + ")");
    }

    /**
     * Récupère le biome pour une position donnée.
     * @param x Coordonnée X.
     * @param z Coordonnée Z.
     * @return Biome pour cette position.
     */
    public Biome getBiome(int x, int z) {
        // Calculer les probabilités cumulées
        float totalProbability = 0.0f;
        for (Float probability : biomeProbabilities.values()) {
            totalProbability += probability;
        }

        // Choisir un biome en fonction des probabilités
        float randomValue = random.nextFloat() * totalProbability;
        float cumulativeProbability = 0.0f;

        for (Map.Entry<BiomeType, Float> entry : biomeProbabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                return biomes.get(entry.getKey());
            }
        }

        // Retourner le biome par défaut (Plaine)
        return biomes.get(BiomeType.PLAINS);
    }

    /**
     * Génère une entité vivante à une position donnée.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param entityType Type de l'entité.
     */
    public void generateLivingEntity(int x, int y, int z, String entityType) {
        LivingEntity entity = new LivingEntity(entityType, world, x, y, z);
        world.addEntity(entity);
    }

    /**
     * Génère une entité à une position donnée.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param entityType Type de l'entité.
     */
    public void generateEntity(int x, int y, int z, String entityType) {
        Entity entity = new Entity(entityType, world, x, y, z) {
            @Override
            public void update(float deltaTime) {

            }
        };
        world.addEntity(entity);
    }

    // --- Getters et Setters ---

    public World getWorld() {
        return world;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    public Random getRandom() {
        return random;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Map<BiomeType, Biome> getBiomes() {
        return biomes;
    }

    public Map<BiomeType, Float> getBiomeProbabilities() {
        return biomeProbabilities;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public float getStructureProbability() {
        return structureProbability;
    }

    public void setStructureProbability(float structureProbability) {
        this.structureProbability = structureProbability;
    }
}