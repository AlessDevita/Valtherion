package fr.alessdevita.valtherion.world.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * Gère le chargement et le déchargement des chunks autour du joueur.
 */
public class ChunkLoader {
    private Map<String, Chunk> loadedChunks;
    private ChunkGenerator generator;
    private int viewDistance;

    /**
     * Crée un ChunkLoader avec une graine.
     * @param seed Graine pour la génération procédurale
     */
    public ChunkLoader(long seed) {
        this.loadedChunks = new HashMap<>();
        this.generator = new ChunkGenerator(seed);
        this.viewDistance = 8;
    }

    /**
     * Récupère ou génère un chunk.
     * @param cx Coordonnée X du chunk
     * @param cz Coordonnée Z du chunk
     * @return Chunk généré ou chargé
     */
    public Chunk getChunk(int cx, int cz) {
        String key = cx + "," + cz;
        if (!loadedChunks.containsKey(key)) {
            Chunk chunk = new Chunk(cx, cz, Chunk.SIZE);
            generator.generate(chunk);
            loadedChunks.put(key, chunk);
        }
        return loadedChunks.get(key);
    }

    /**
     * Décharge les chunks trop loin du joueur.
     * @param playerX Coordonnée X du joueur
     * @param playerZ Coordonnée Z du joueur
     * @param viewDistance Distance de vue (en chunks)
     */
    public void unloadFarChunks(int playerX, int playerZ, int viewDistance) {
        this.viewDistance = viewDistance;
        int playerChunkX = playerX >> 4;
        int playerChunkZ = playerZ >> 4;
        
        Iterator<String> iterator = loadedChunks.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String[] parts = key.split(",");
            int chunkX = Integer.parseInt(parts[0]);
            int chunkZ = Integer.parseInt(parts[1]);
            
            int distance = Math.max(Math.abs(chunkX - playerChunkX), Math.abs(chunkZ - playerChunkZ));
            if (distance > viewDistance) {
                Chunk chunk = loadedChunks.get(key);
                chunk.cleanup();
                iterator.remove();
            }
        }
    }

    /**
     * Retourne le nombre de chunks chargés.
     * @return Nombre de chunks
     */
    public int getLoadedChunkCount() {
        return loadedChunks.size();
    }

    /**
     * Précharge les chunks autour d'une position.
     * @param centerX Coordonnée X du centre
     * @param centerZ Coordonnée Z du centre
     * @param distance Distance autour du centre
     */
    public void preloadChunks(int centerX, int centerZ, int distance) {
        int centerChunkX = centerX >> 4;
        int centerChunkZ = centerZ >> 4;
        
        for (int x = centerChunkX - distance; x <= centerChunkX + distance; x++) {
            for (int z = centerChunkZ - distance; z <= centerChunkZ + distance; z++) {
                getChunk(x, z);
            }
        }
    }

    /**
     * Décharge tous les chunks.
     */
    public void unloadAll() {
        for (Chunk chunk : loadedChunks.values()) {
            chunk.cleanup();
        }
        loadedChunks.clear();
    }
}