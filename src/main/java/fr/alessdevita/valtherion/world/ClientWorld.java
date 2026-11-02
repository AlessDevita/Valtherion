package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Monde côté client.
 * Gère le rendu et la synchronisation avec le serveur.
 */
public class ClientWorld {

    private final Map<String, Chunk> chunks;
    private World.WorldState state;
    private float worldTime;
    private boolean isRemote;

    public ClientWorld() {
        this.chunks = new HashMap<>();
        this.state = World.WorldState.DAY;
        this.worldTime = 0.0f;
        this.isRemote = false;
    }

    /**
     * Met à jour le monde client.
     */
    public void tick() {
        // Mettre à jour les chunks
        for (Chunk chunk : chunks.values()) {
            if (chunk != null) {
                chunk.update();
            }
        }
    }

    /**
     * Ajoute un chunk au monde client.
     * @param chunk Chunk à ajouter
     */
    public void addChunk(Chunk chunk) {
        if (chunk != null) {
            String key = chunk.getX() + "," + chunk.getZ();
            chunks.put(key, chunk);
        }
    }

    /**
     * Retire un chunk du monde client.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     */
    public void removeChunk(int x, int z) {
        String key = x + "," + z;
        Chunk chunk = chunks.remove(key);
        if (chunk != null) {
            chunk.cleanup();
        }
    }

    /**
     * Récupère un chunk.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return Chunk ou null
     */
    public Chunk getChunk(int x, int z) {
        String key = x + "," + z;
        return chunks.get(key);
    }

    /**
     * Récupère un bloc.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @return Bloc ou null
     */
    public Block getBlock(int x, int y, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        Chunk chunk = getChunk(chunkX, chunkZ);
        if (chunk == null) return null;

        int localX = Math.abs(x % 16);
        int localZ = Math.abs(z % 16);
        return chunk.getBlock(localX, y, localZ);
    }

    public void setState(World.WorldState state) { this.state = state; }
    public World.WorldState getState() { return state; }
    public void setWorldTime(float worldTime) { this.worldTime = worldTime; }
    public float getWorldTime() { return worldTime; }
    public void setRemote(boolean isRemote) { this.isRemote = isRemote; }
    public boolean isRemote() { return isRemote; }
    public int getChunkCount() { return chunks.size(); }
}
