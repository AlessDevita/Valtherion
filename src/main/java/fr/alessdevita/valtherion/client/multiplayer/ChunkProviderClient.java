package fr.alessdevita.valtherion.client.multiplayer;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Cache des chunks reçus par le client depuis le serveur. */
public class ChunkProviderClient {
    private final World world;
    private final Map<Long, Chunk> loadedChunks = new ConcurrentHashMap<>();

    public ChunkProviderClient() { this(null); }

    public ChunkProviderClient(World world) { this.world = world; }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        long key = chunkKey(chunkX, chunkZ);
        return this.loadedChunks.computeIfAbsent(key, ignored -> {
            Chunk chunk = new Chunk(chunkX, chunkZ, Chunk.SIZE);
            if (this.world != null) chunk.setWorld(this.world);
            return chunk;
        });
    }

    public Chunk provideChunk(int chunkX, int chunkZ) { return loadChunk(chunkX, chunkZ); }
    public Chunk getLoadedChunk(int chunkX, int chunkZ) { return this.loadedChunks.get(chunkKey(chunkX, chunkZ)); }
    public boolean chunkExists(int chunkX, int chunkZ) { return this.loadedChunks.containsKey(chunkKey(chunkX, chunkZ)); }

    public boolean unloadChunk(int chunkX, int chunkZ) {
        return this.loadedChunks.remove(chunkKey(chunkX, chunkZ)) != null;
    }

    public void unloadAllChunks() { this.loadedChunks.clear(); }
    public int getLoadedChunkCount() { return this.loadedChunks.size(); }
    public Collection<Chunk> getLoadedChunks() {
        return Collections.unmodifiableCollection(this.loadedChunks.values());
    }

    private static long chunkKey(int x, int z) {
        return ((long) x & 0xFFFFFFFFL) | (((long) z & 0xFFFFFFFFL) << 32);
    }
}
