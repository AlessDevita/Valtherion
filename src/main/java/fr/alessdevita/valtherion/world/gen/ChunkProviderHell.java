package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;
import fr.alessdevita.valtherion.world.chunk.ChunkGenerator;

/**
 * Fournisseur de chunks pour le Nether.
 * Génère du terrain bas avec des grottes et de la lave.
 */
public class ChunkProviderHell {

    private final World world;
    private final ChunkGenerator generator;
    private final MapGenCavesHell caves;

    public ChunkProviderHell(World world, long seed) {
        this.world = world;
        this.generator = new ChunkGenerator(seed);
        this.caves = new MapGenCavesHell();
        this.caves.init(world);
    }

    public Chunk generateChunk(int x, int z) {
        Chunk chunk = new Chunk(x, z, Chunk.SIZE);
        generator.generate(chunk);
        caves.generate(chunk, x, z);
        return chunk;
    }
}
