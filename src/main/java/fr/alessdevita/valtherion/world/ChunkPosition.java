package fr.alessdevita.valtherion.world;

import java.util.Objects;

/**
 * Position immuable dans un chunk (x, y, z locaux).
 * Utilisée pour identifier un bloc précis à l'intérieur d'un chunk.
 */
public final class ChunkPosition {

    private final int x;
    private final int y;
    private final int z;

    /**
     * Crée une position dans un chunk.
     * @param x Coordonnée X locale (0-15)
     * @param y Coordonnée Y (0-255)
     * @param z Coordonnée Z locale (0-15)
     */
    public ChunkPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Crée une position à partir d'une position mondiale.
     * @param worldX Coordonnée X mondiale
     * @param worldY Coordonnée Y mondiale
     * @param worldZ Coordonnée Z mondiale
     * @return Position locale dans le chunk
     */
    public static ChunkPosition fromWorldCoords(int worldX, int worldY, int worldZ) {
        return new ChunkPosition(
                Math.abs(worldX % 16),
                worldY,
                Math.abs(worldZ % 16)
        );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    /**
     * Vérifie si la position est valide dans un chunk.
     * @return true si les coordonnées sont dans les limites
     */
    public boolean isValid() {
        return x >= 0 && x < 16 && y >= 0 && y < 256 && z >= 0 && z < 16;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkPosition that = (ChunkPosition) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "ChunkPosition{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
