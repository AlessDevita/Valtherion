package fr.alessdevita.valtherion.world.chunk;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.Block;

/**
 * Une section 16x16x256 du monde.
 * Représente une unité de base pour la gestion du terrain.
 */
public class Chunk {
    public static final int SIZE = 16;
    public static final int HEIGHT = 256;
    private final int chunkX, chunkZ;
    private final Block[][][] blocks; // x, y, z
    private World world;
    private boolean dirty;

    /**
     * Crée un nouveau chunk.
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     * @param chunkSize Taille du chunk (généralement SIZE)
     */
    public Chunk(int chunkX, int chunkZ, int chunkSize) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.blocks = new Block[SIZE][HEIGHT][SIZE];
        this.dirty = true;
    }

    /**
     * Définit un bloc à une position donnée.
     * @param x Coordonnée X locale (0-15)
     * @param y Coordonnée Y (0-255)
     * @param z Coordonnée Z locale (0-15)
     * @param block Bloc à placer
     */
    public void setBlock(int x, int y, int z, Block block) {
        if (isValid(x, y, z)) {
            blocks[x][y][z] = block;
            dirty = true;
        }
    }

    /**
     * Récupère un bloc à une position donnée.
     * @param x Coordonnée X locale (0-15)
     * @param y Coordonnée Y (0-255)
     * @param z Coordonnée Z locale (0-15)
     * @return Bloc à la position ou null
     */
    public Block getBlock(int x, int y, int z) {
        if (isValid(x, y, z)) {
            return blocks[x][y][z];
        }
        return null;
    }

    /**
     * Vérifie si les coordonnées sont valides dans ce chunk.
     * @param x Coordonnée X locale
     * @param y Coordonnée Y
     * @param z Coordonnée Z locale
     * @return true si les coordonnées sont valides
     */
    private boolean isValid(int x, int y, int z) {
        return x >= 0 && x < SIZE && y >= 0 && y < HEIGHT && z >= 0 && z < SIZE;
    }

    /**
     * Retourne la coordonnée X du chunk.
     * @return Coordonnée X du chunk
     */
    public int getChunkX() {
        return chunkX;
    }

    /**
     * Retourne la coordonnée Z du chunk.
     * @return Coordonnée Z du chunk
     */
    public int getChunkZ() {
        return chunkZ;
    }

    /**
     * Retourne la coordonnée X du chunk.
     * @return Coordonnée X du chunk
     */
    public int getX() {
        return chunkX;
    }

    /**
     * Retourne la coordonnée Z du chunk.
     * @return Coordonnée Z du chunk
     */
    public int getZ() {
        return chunkZ;
    }

    /**
     * Met à jour le chunk (mise à jour des blocs).
     */
    public void update() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < SIZE; z++) {
                    Block block = blocks[x][y][z];
                    if (block != null) {
                        block.update(0.016f);
                    }
                }
            }
        }
    }

    /**
     * Définit le monde auquel ce chunk appartient.
     * @param world Monde
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Retourne le monde auquel ce chunk appartient.
     * @return Monde ou null
     */
    public World getWorld() {
        return world;
    }

    /**
     * Indique si le chunk a besoin d'être regénéré visuellement.
     * @return true si le chunk est modifié
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Marque le chunk comme propre (mesh à jour).
     */
    public void markClean() {
        dirty = false;
    }

    /**
     * Libère les ressources du chunk.
     */
    public void cleanup() {
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                for (int y = 0; y < HEIGHT; y++) {
                    blocks[x][y][z] = null;
                }
            }
        }
    }

    /**
     * Retourne le nombre total de blocs dans le chunk.
     * @return Nombre de blocs
     */
    public int getBlockCount() {
        return SIZE * HEIGHT * SIZE;
    }
}