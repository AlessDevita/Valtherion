package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.world.chunk.Chunk;
import fr.alessdevita.valtherion.world.gen.ChunkProviderGenerate;
import fr.alessdevita.valtherion.world.gen.structure.MapGenMineshaft;
import fr.alessdevita.valtherion.world.gen.structure.MapGenVillage;
import fr.alessdevita.valtherion.world.gen.structure.MapGenScatteredFeature;
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
 * Utilise ChunkProviderGenerate pour créer un monde aléatoire style Minecraft avec :
 * - Terrain généré par bruit Perlin multi-octaves
 * - Biomes variés (plaines, forêts, montagnes, déserts, neige, jungle, marais, océan)
 * - Grottes souterraines
 * - Minerais (charbon, fer, or, diamant)
 * - Eau et lave
 * - Décorations (arbres, fleurs, cactus, champignons)
 * - Structures (villages, mines, temples)
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

    // Fournisseur de chunks pour la génération
    private ChunkProviderGenerate chunkProvider;

    // Générateurs de structures
    private MapGenMineshaft mineshaft;
    private MapGenVillage village;
    private MapGenScatteredFeature scatteredFeature;

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
        this.maxHeight = 256;
        this.biomes = new HashMap<>();
        this.biomeProbabilities = new HashMap<>();
        this.structures = new ArrayList<>();
        this.structureProbability = 0.01f;

        // Initialiser le fournisseur de chunks
        this.chunkProvider = new ChunkProviderGenerate(world, seed);

        // Initialiser les générateurs de structures
        this.mineshaft = new MapGenMineshaft(world, seed);
        this.village = new MapGenVillage(world, seed);
        this.scatteredFeature = new MapGenScatteredFeature(world, seed, 0);

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
        this.chunkProvider = new ChunkProviderGenerate(world, seed);
        this.mineshaft = new MapGenMineshaft(world, seed);
        this.village = new MapGenVillage(world, seed);
        this.scatteredFeature = new MapGenScatteredFeature(world, seed, 0);
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
        biomeProbabilities.put(BiomeType.PLAINS, 0.3f);

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
        biomeProbabilities.put(BiomeType.FOREST, 0.25f);

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

        // Biome de neige
        Biome snowBiome = new Biome(BiomeType.SNOW, "Neige", "Un biome enneigé.");
        snowBiome.setBaseHeight(68);
        snowBiome.setHeightVariation(8);
        snowBiome.addBlock(BlockType.SNOW, 0.7f);
        snowBiome.addBlock(BlockType.ICE, 0.2f);
        snowBiome.addBlock(BlockType.STONE, 0.1f);
        biomes.put(BiomeType.SNOW, snowBiome);
        biomeProbabilities.put(BiomeType.SNOW, 0.1f);

        // Biome de jungle
        Biome jungleBiome = new Biome(BiomeType.JUNGLE, "Jungle", "Un biome de jungle dense.");
        jungleBiome.setBaseHeight(72);
        jungleBiome.setHeightVariation(12);
        jungleBiome.addBlock(BlockType.GRASS, 0.5f);
        jungleBiome.addBlock(BlockType.WOOD, 0.2f);
        jungleBiome.addBlock(BlockType.LEAVES, 0.2f);
        jungleBiome.addBlock(BlockType.VINE, 0.1f);
        biomes.put(BiomeType.JUNGLE, jungleBiome);
        biomeProbabilities.put(BiomeType.JUNGLE, 0.05f);

        // Biome de marais
        Biome swampBiome = new Biome(BiomeType.SWAMP, "Marais", "Un biome humide et marécageux.");
        swampBiome.setBaseHeight(62);
        swampBiome.setHeightVariation(2);
        swampBiome.addBlock(BlockType.GRASS, 0.4f);
        swampBiome.addBlock(BlockType.WATER, 0.3f);
        swampBiome.addBlock(BlockType.DIRT, 0.2f);
        swampBiome.addBlock(BlockType.MUSHROOM, 0.1f);
        biomes.put(BiomeType.SWAMP, swampBiome);
        biomeProbabilities.put(BiomeType.SWAMP, 0.05f);

        // Biome d'océan
        Biome oceanBiome = new Biome(BiomeType.OCEAN, "Océan", "Un biome aquatique profond.");
        oceanBiome.setBaseHeight(50);
        oceanBiome.setHeightVariation(5);
        oceanBiome.addBlock(BlockType.SAND, 0.3f);
        oceanBiome.addBlock(BlockType.WATER, 0.6f);
        oceanBiome.addBlock(BlockType.GRAVEL, 0.1f);
        biomes.put(BiomeType.OCEAN, oceanBiome);
        biomeProbabilities.put(BiomeType.OCEAN, 0.05f);
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
                treeStructure[x][4][z] = BlockType.LEAVES;
            }
        }

        structures.add(new Structure("Arbre", treeStructure));
    }

    /**
     * Génère le monde initial.
     */
    public void generateInitialWorld() {
        generateInitialChunks(5);

        System.out.println("Monde initial généré avec la graine : " + seed);
    }

    /**
     * Génère les chunks autour de l'origine sans créer de joueur serveur.
     */
    public void generateInitialChunks(int renderDistance) {
        if (renderDistance < 0) {
            throw new IllegalArgumentException("La distance de génération ne peut pas être négative");
        }
        System.out.println("Génération du monde... (" + (renderDistance * 2 + 1) + "x" + (renderDistance * 2 + 1) + " chunks)");

        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {
                generateChunk(x, z);
            }
        }

        System.out.println("Génération du monde terminée.");
    }

    /**
     * Génère un chunk en utilisant ChunkProviderGenerate.
     * @param chunkX Coordonnée X du chunk.
     * @param chunkZ Coordonnée Z du chunk.
     */
    public void generateChunk(int chunkX, int chunkZ) {
        Chunk chunk = world.getChunk(chunkX, chunkZ);
        if (chunk != null) {
            return; // Le chunk existe déjà
        }

        // Utiliser ChunkProviderGenerate pour générer le chunk
        chunk = chunkProvider.generateChunk(chunkX, chunkZ);

        // Générer les structures pour le chunk
        generateStructures(chunk, chunkX, chunkZ);

        // Ajouter le chunk au monde
        world.addChunk(chunk);
    }

    /**
     * Génère les structures pour un chunk.
     * @param chunk Chunk à générer.
     * @param chunkX Coordonnée X du chunk.
     * @param chunkZ Coordonnée Z du chunk.
     */
    private void generateStructures(Chunk chunk, int chunkX, int chunkZ) {
        // Générer les mines souterraines
        mineshaft.generate(chunkX, chunkZ);

        // Générer les villages
        village.generate(chunkX, chunkZ);

        // Générer les structures dispersées (temples, ruines)
        scatteredFeature.generate(chunkX, chunkZ);
    }

    /**
     * Génère le joueur initial.
     */
    private void generateInitialPlayer() {
        // Trouver une position de spawn valide
        int spawnX = 0;
        int spawnY = 64;
        int spawnZ = 0;

        // Monter jusqu'à trouver un endroit libre
        while (spawnY < 200) {
            if (!world.isBlockSolid(spawnX, spawnY, spawnZ)) {
                // Vérifier que le bloc en dessous est solide
                if (world.isBlockSolid(spawnX, spawnY - 1, spawnZ)) {
                    break;
                }
            }
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
        // Utiliser un hash déterministe basé sur la position
        long hash = seed ^ ((long)x * 0x9E3779B97F4A7C15L) ^ ((long)z * 0xC2B2AE3D27D4EB4FL);
        Random biomeRandom = new Random(hash);

        // Calculer les probabilités cumulées
        float totalProbability = 0.0f;
        for (Float probability : biomeProbabilities.values()) {
            totalProbability += probability;
        }

        // Choisir un biome en fonction des probabilités
        float randomValue = biomeRandom.nextFloat() * totalProbability;
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
        this.chunkProvider = new ChunkProviderGenerate(world, seed);
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

    public ChunkProviderGenerate getChunkProvider() {
        return chunkProvider;
    }
}