package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;

/**
 * Fournisseur de chunks pour l'End.
 * Génère des îles flottantes dans le vide.
 */
public class ChunkProviderEnd {

    private final World world;

    public ChunkProviderEnd(World world) {
        this.world = world;
    }

    public Chunk generateChunk(int x, int z) {
        Chunk chunk = new Chunk(x, z, Chunk.SIZE);

        // L'île centrale est au chunk (0, 0)
        if (x == 0 && z == 0) {
            generateIsland(chunk);
        } else {
            // Générer des îles flottantes aléatoires pour les autres chunks
            generateFloatingIslands(chunk);
        }

        return chunk;
    }

    private void generateFloatingIslands(Chunk chunk) {
        // Générer des îles flottantes aléatoires
        for (int lx = 0; lx < Chunk.SIZE; lx++) {
            for (int lz = 0; lz < Chunk.SIZE; lz++) {
                double noise = Math.sin(lx * 0.5) * Math.cos(lz * 0.5);
                if (noise > 0.7) {
                    int height = 60 + (int) (noise * 10);
                    for (int y = height - 5; y <= height; y++) {
                        chunk.setBlock(lx, y, lz, createBlock(fr.alessdevita.valtherion.block.BlockType.STONE));
                    }
                }
            }
        }
    }

    private void generateIsland(Chunk chunk) {
        for (int lx = 0; lx < Chunk.SIZE; lx++) {
            for (int lz = 0; lz < Chunk.SIZE; lz++) {
                for (int y = 0; y < 80; y++) {
                    chunk.setBlock(lx, y, lz, createBlock(fr.alessdevita.valtherion.block.BlockType.STONE));
                }
            }
        }
    }

    private fr.alessdevita.valtherion.block.Block createBlock(fr.alessdevita.valtherion.block.BlockType type) {
        return new fr.alessdevita.valtherion.block.Block(type) {
            @Override
            public void update(float deltaTime) {}
        };
    }
}
