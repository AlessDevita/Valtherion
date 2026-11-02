package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;
import fr.alessdevita.valtherion.world.chunk.ChunkGenerator;

import java.util.Random;

/**
 * Fournisseur de chunks pour le monde de surface (Overworld).
 * Génère du terrain avec bruit Perlin et biomes.
 */
public class ChunkProviderGenerate {

    private final World world;
    private final ChunkGenerator generator;
    private final MapGenCaves caves;
    private final MapGenRavine ravine;
    private final Random random;

    public ChunkProviderGenerate(World world, long seed) {
        this.world = world;
        this.generator = new ChunkGenerator(seed);
        this.caves = new MapGenCaves();
        this.caves.init(world);
        this.ravine = new MapGenRavine();
        this.ravine.init(world);
        this.random = new Random(seed);
    }

    /**
     * Génère un chunk.
     * @param x Coordonnée X du chunk
     * @param z Coordonnée Z du chunk
     * @return Chunk généré
     */
    public Chunk generateChunk(int x, int z) {
        Chunk chunk = new Chunk(x, z, Chunk.SIZE);
        generator.generate(chunk);
        caves.generate(chunk, x, z);
        ravine.generate(chunk, x, z);
        return chunk;
    }

    /**
     * Génère les chunks autour d'une position.
     * @param playerX Position X du joueur
     * @param playerZ Position Z du joueur
     * @param viewDistance Distance de vue
     */
    public void generateChunksAround(int playerX, int playerZ, int viewDistance) {
        int chunkX = playerX >> 4;
        int chunkZ = playerZ >> 4;

        for (int x = chunkX - viewDistance; x <= chunkX + viewDistance; x++) {
            for (int z = chunkZ - viewDistance; z <= chunkZ + viewDistance; z++) {
                if (world.getChunk(x, z) == null) {
                    world.addChunk(generateChunk(x, z));
                }
            }
        }
    }
}
