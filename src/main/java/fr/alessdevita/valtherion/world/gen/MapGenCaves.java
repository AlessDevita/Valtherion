package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.world.chunk.Chunk;

/**
 * Générateur de grottes souterraines.
 * Crée des tunnels et des cavernes dans le terrain.
 */
public class MapGenCaves extends MapGenBase {

    private static final int MAX_RADIUS = 4;

    @Override
    public void generate(Chunk chunk, int x, int z) {
        int attempts = 10;
        for (int i = 0; i < attempts; i++) {
            int blockX = x * Chunk.SIZE + random.nextInt(Chunk.SIZE);
            int blockY = random.nextInt(128);
            int blockZ = z * Chunk.SIZE + random.nextInt(Chunk.SIZE);

            double noiseValue = new NoiseGeneratorPerlin(random).noise(blockX * 0.05, blockZ * 0.05);
            if (noiseValue > 0.3) {
                generateTunnel(chunk, blockX, blockY, blockZ);
            }
        }
    }

    private void generateTunnel(Chunk chunk, int startX, int startY, int startZ) {
        int length = 20 + random.nextInt(40);
        int x = startX, y = startY, z = startZ;

        for (int i = 0; i < length; i++) {
            int radius = MAX_RADIUS - (random.nextInt(3));
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        if (dx * dx + dy * dy + dz * dz <= radius * radius) {
                            int wx = x + dx;
                            int wy = y + dy;
                            int wz = z + dz;

                            int cx = wx & 15;
                            int cz = wz & 15;
                            if (cx >= 0 && cx < Chunk.SIZE && wy >= 0 && wy < Chunk.HEIGHT && cz >= 0 && cz < Chunk.SIZE) {
                                chunk.setBlock(cx, wy, cz, createBlock(BlockType.AIR));
                            }
                        }
                    }
                }
            }

            x += random.nextInt(3) - 1;
            y += random.nextInt(3) - 1;
            z += random.nextInt(3) - 1;
        }
    }

    private fr.alessdevita.valtherion.block.Block createBlock(BlockType type) {
        return new fr.alessdevita.valtherion.block.Block(type) {
            @Override
            public void update(float deltaTime) {}
        };
    }
}
