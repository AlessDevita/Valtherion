package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.world.chunk.Chunk;

/**
 * Générateur de grottes pour le Nether.
 * Crée des tunnels plus étroits et des lacs de lave.
 */
public class MapGenCavesHell extends MapGenBase {

    @Override
    public void generate(Chunk chunk, int x, int z) {
        int attempts = 5;
        for (int i = 0; i < attempts; i++) {
            int blockX = x * Chunk.SIZE + random.nextInt(Chunk.SIZE);
            int blockY = 32 + random.nextInt(64);
            int blockZ = z * Chunk.SIZE + random.nextInt(Chunk.SIZE);

            generateHellTunnel(chunk, blockX, blockY, blockZ);
        }
    }

    private void generateHellTunnel(Chunk chunk, int startX, int startY, int startZ) {
        int length = 10 + random.nextInt(20);
        int x = startX, y = startY, z = startZ;

        for (int i = 0; i < length; i++) {
            int radius = 2;
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
                                // Chance de créer de la lave au fond
                                BlockType type = (dy == -radius && random.nextBoolean()) ? BlockType.LAVA : BlockType.AIR;
                                chunk.setBlock(cx, wy, cz, createBlock(type));
                            }
                        }
                    }
                }
            }

            x += random.nextInt(3) - 1;
            y += random.nextInt(2) - 1;
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
