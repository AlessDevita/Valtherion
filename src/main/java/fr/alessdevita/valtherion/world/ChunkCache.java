package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Cache de blocs pour un chunk et ses voisins.
 * Permet aux blocs d'accéder rapidement aux blocs adjacents sans traverser les limites de chunk.
 */
public class ChunkCache {

    private final int centerX;
    private final int centerZ;
    private final int radius;
    private final Map<String, Block> blockCache;

    /**
     * Crée un cache de chunk avec un rayon de 1 (chunk central + voisins).
     * @param world Monde source
     * @param chunkX Coordonnée X du chunk central
     * @param chunkZ Coordonnée Z du chunk central
     */
    public ChunkCache(World world, int chunkX, int chunkZ) {
        this(world, chunkX, chunkZ, 1);
    }

    /**
     * Crée un cache de chunk avec un rayon configurable.
     * @param world Monde source
     * @param chunkX Coordonnée X du chunk central
     * @param chunkZ Coordonnée Z du chunk central
     * @param radius Rayon du cache en chunks
     */
    public ChunkCache(World world, int chunkX, int chunkZ, int radius) {
        this.centerX = chunkX * Chunk.SIZE;
        this.centerZ = chunkZ * Chunk.SIZE;
        this.radius = radius;
        this.blockCache = new HashMap<>();

        // Précharger les chunks dans le rayon
        for (int cx = chunkX - radius; cx <= chunkX + radius; cx++) {
            for (int cz = chunkZ - radius; cz <= chunkZ + radius; cz++) {
                Chunk chunk = world.getChunk(cx, cz);
                if (chunk != null) {
                    for (int x = 0; x < Chunk.SIZE; x++) {
                        for (int y = 0; y < Chunk.HEIGHT; y++) {
                            for (int z = 0; z < Chunk.SIZE; z++) {
                                Block block = chunk.getBlock(x, y, z);
                                if (block != null) {
                                    int worldX = cx * Chunk.SIZE + x;
                                    int worldZ = cz * Chunk.SIZE + z;
                                    blockCache.put(worldX + "," + y + "," + worldZ, block);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Récupère un bloc depuis le cache.
     * @param x Coordonnée X mondiale
     * @param y Coordonnée Y
     * @param z Coordonnée Z mondiale
     * @return Bloc ou null
     */
    public Block getBlock(int x, int y, int z) {
        return blockCache.get(x + "," + y + "," + z);
    }

    /**
     * Retourne le centre X du cache (monde).
     * @return Coordonnée X
     */
    public int getCenterX() {
        return centerX;
    }

    /**
     * Retourne le centre Z du cache (monde).
     * @return Coordonnée Z
     */
    public int getCenterZ() {
        return centerZ;
    }

    /**
     * Retourne le rayon du cache.
     * @return Rayon en chunks
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Retourne le nombre de blocs en cache.
     * @return Nombre de blocs
     */
    public int size() {
        return blockCache.size();
    }

    /**
     * Vide le cache.
     */
    public void clear() {
        blockCache.clear();
    }
}
