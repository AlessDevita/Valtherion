package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.world.chunk.Chunk;

/**
 * Générateur de ravins.
 * Crée des ravins profonds et étroits dans le terrain.
 */
public class MapGenRavine extends MapGenBase {

    @Override
    public void generate(Chunk chunk, int x, int z) {
        // Vérifier si un ravin traverse ce chunk
        double noiseX = new NoiseGeneratorPerlin(random).noise(x * 0.25, 0);
        double noiseZ = new NoiseGeneratorPerlin(random).noise(0, z * 0.25);

        if (Math.abs(noiseX) < 0.02 && Math.abs(noiseZ) < 0.02) {
            generateRavine(chunk, x, z);
        }
    }

    private void generateRavine(Chunk chunk, int chunkX, int chunkZ) {
        int width = 4 + random.nextInt(4);
        int depth = 20 + random.nextInt(20);

        for (int localX = 0; localX < Chunk.SIZE; localX++) {
            for (int localZ = 0; localZ < Chunk.SIZE; localZ++) {
                // Distance du centre du chunk
                double distX = Math.abs(localX - Chunk.SIZE / 2) / (Chunk.SIZE / 2.0);
                double distZ = Math.abs(localZ - Chunk.SIZE / 2) / (Chunk.SIZE / 2.0);
                double distance = Math.sqrt(distX * distX + distZ * distZ);

                if (distance < 0.5) {
                    // Creuser le ravin
                    for (int y = 0; y < Chunk.HEIGHT; y++) {
                        if (y < depth || (y < depth + 10 && random.nextBoolean())) {
                            int carveWidth = (int) ((0.5 - distance) * width);
                            if (carveWidth > 0) {
                                chunk.setBlock(localX, y, localZ, createBlock(BlockType.AIR));
                            }
                        }
                    }
                }
            }
        }
    }

    private fr.alessdevita.valtherion.block.Block createBlock(BlockType type) {
        return new fr.alessdevita.valtherion.block.Block(type) {
            @Override
            public void update(float deltaTime) {}
        };
    }
}
