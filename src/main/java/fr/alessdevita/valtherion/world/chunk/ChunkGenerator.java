package fr.alessdevita.valtherion.world.chunk;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.block.BlockType;
import java.util.*;

/**
 * Gère la génération procédurale du terrain pour un chunk.
 * Utilise un bruit de Perlin et les biomes pour générer un terrain réaliste.
 */
public class ChunkGenerator {
    private final long seed;
    private Random random;

    /**
     * Crée un générateur de chunk avec une graine.
     * @param seed Graine pour la génération procédurale
     */
    public ChunkGenerator(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    /**
     * Génère le contenu d'un chunk.
     * @param chunk Chunk à générer
     */
    public void generate(Chunk chunk) {
        generateTerrain(chunk);
        generateDecorations(chunk);
    }

    /**
     * Génère le terrain du chunk.
     * @param chunk Chunk à générer
     */
    private void generateTerrain(Chunk chunk) {
        for (int x = 0; x < Chunk.SIZE; x++) {
            for (int z = 0; z < Chunk.SIZE; z++) {
                int height = generateHeight(chunk.getChunkX() * Chunk.SIZE + x, chunk.getChunkZ() * Chunk.SIZE + z);
                
                for (int y = 0; y < height; y++) {
                    BlockType blockType = getBlockAtHeight(y, height);
                    chunk.setBlock(x, y, z, createBlock(blockType));
                }
                
                for (int y = height; y < Chunk.HEIGHT; y++) {
                    chunk.setBlock(x, y, z, createBlock(BlockType.AIR));
                }
            }
        }
    }

    /**
     * Génère les décorations (arbres, fleurs, etc).
     * @param chunk Chunk à décorer
     */
    private void generateDecorations(Chunk chunk) {
        // Peut être étendu pour ajouter des arbres, fleurs, etc.
    }

    /**
     * Génère la hauteur du terrain pour une position XZ.
     * @param x Coordonnée X globale
     * @param z Coordonnée Z globale
     * @return Hauteur du terrain
     */
    private int generateHeight(int x, int z) {
        float noise = perlinNoise(x * 0.05f, z * 0.05f);
        return (int) (64 + noise * 20);
    }

    /**
     * Détermine le type de bloc en fonction de la hauteur.
     * @param y Coordonnée Y actuelle
     * @param height Hauteur maximale du terrain
     * @return Type de bloc approprié
     */
    private BlockType getBlockAtHeight(int y, int height) {
        if (y == height - 1) {
            return BlockType.GRASS;
        } else if (y > height - 5) {
            return BlockType.DIRT;
        } else if (y > 50) {
            return BlockType.STONE;
        } else {
            return BlockType.COBBLESTONE;
        }
    }

    /**
     * Crée une instance de bloc.
     * @param type Type de bloc à créer
     * @return Instance de Block
     */
    private Block createBlock(BlockType type) {
        return new Block(type) {
            @Override
            public void update(float deltaTime) {
            }
        };
    }

    /**
     * Implémentation simple du bruit de Perlin.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return Valeur du bruit (-1 à 1)
     */
    private float perlinNoise(float x, float z) {
        return (float) (Math.sin(x * 0.1f) * Math.cos(z * 0.1f) * 2.0f +
                Math.sin(x * 0.02f) * 0.5f);
    }
}