package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;

/**
 * Fournisseur de chunks pour un monde plat (Superflat).
 * Génère un terrain plat avec des couches configurables.
 */
public class ChunkProviderFlat {

    private final World world;
    private final FlatGeneratorInfo info;

    public ChunkProviderFlat(World world, FlatGeneratorInfo info) {
        this.world = world;
        this.info = info != null ? info : new FlatGeneratorInfo();
    }

    public Chunk generateChunk(int x, int z) {
        Chunk chunk = new Chunk(x, z, Chunk.SIZE);

        for (FlatLayerInfo layer : info.getLayers()) {
            for (int lx = 0; lx < Chunk.SIZE; lx++) {
                for (int lz = 0; lz < Chunk.SIZE; lz++) {
                    for (int y = layer.getStartHeight(); y < layer.getStartHeight() + layer.getThickness(); y++) {
                        if (y >= 0 && y < Chunk.HEIGHT) {
                            chunk.setBlock(lx, y, lz, createBlock(layer.getBlockType()));
                        }
                    }
                }
            }
        }

        return chunk;
    }

    private fr.alessdevita.valtherion.block.Block createBlock(fr.alessdevita.valtherion.block.BlockType type) {
        return new fr.alessdevita.valtherion.block.Block(type) {
            @Override
            public void update(float deltaTime) {}
        };
    }
}
