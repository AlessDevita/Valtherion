package fr.alessdevita.valtherion.world;

import java.util.Objects;

/**
 * Paire immuable de coordonnées de chunk (X, Z).
 * Utilisée comme clé dans les maps pour identifier un chunk de manière unique.
 */
public final class ChunkCoordIntPair {

    private final int chunkX;
    private final int chunkZ;

    /**
     * Crée une paire de coordonnées de chunk.
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     */
    public ChunkCoordIntPair(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    /**
     * Crée une paire à partir d'une position mondiale.
     * @param x Coordonnée X mondiale
     * @param z Coordonnée Z mondiale
     * @return Paire de coordonnées de chunk
     */
    public static ChunkCoordIntPair fromWorldCoords(int x, int z) {
        return new ChunkCoordIntPair(x >> 4, z >> 4);
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    /**
     * Retourne la clé string associée (x,z).
     * @return Clé string
     */
    public String toKey() {
        return chunkX + "," + chunkZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkCoordIntPair that = (ChunkCoordIntPair) o;
        return chunkX == that.chunkX && chunkZ == that.chunkZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkX, chunkZ);
    }

    @Override
    public String toString() {
        return "ChunkCoordIntPair{" + "chunkX=" + chunkX + ", chunkZ=" + chunkZ + '}';
    }
}
