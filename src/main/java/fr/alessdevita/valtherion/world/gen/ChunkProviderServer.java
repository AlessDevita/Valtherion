package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;

/**
 * Fournisseur de chunks côté serveur.
 * Délègue la génération au provider approprié selon le type de monde.
 */
public class ChunkProviderServer {

    private final World world;
    private final Object delegate;

    public ChunkProviderServer(World world, Object delegate) {
        this.world = world;
        this.delegate = delegate;
    }

    public Chunk provideChunk(int x, int z) {
        Chunk existing = world.getChunk(x, z);
        if (existing != null) {
            return existing;
        }

        Chunk chunk;
        if (delegate instanceof ChunkProviderGenerate) {
            chunk = ((ChunkProviderGenerate) delegate).generateChunk(x, z);
        } else if (delegate instanceof ChunkProviderHell) {
            chunk = ((ChunkProviderHell) delegate).generateChunk(x, z);
        } else if (delegate instanceof ChunkProviderEnd) {
            chunk = ((ChunkProviderEnd) delegate).generateChunk(x, z);
        } else if (delegate instanceof ChunkProviderFlat) {
            chunk = ((ChunkProviderFlat) delegate).generateChunk(x, z);
        } else {
            chunk = new Chunk(x, z, Chunk.SIZE);
        }

        world.addChunk(chunk);
        return chunk;
    }

    public Object getDelegate() {
        return delegate;
    }
}
