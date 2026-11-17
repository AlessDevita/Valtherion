package fr.alessdevita.valtherion.world.gen.structure;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Générateur de mines souterraines.
 * Crée des réseaux de couloirs et de pièces souterraines.
 */
public class MapGenMineshaft extends MapGenStructure {

    private boolean hasStonerecess;
    private boolean hasStonerecess2;

    /**
     * Crée un générateur de mines.
     * @param world Monde associé
     * @param seed Graine pour la génération des mines
     */
    public MapGenMineshaft(World world, long seed) {
        super(world, seed);
        this.spacing = 40;
        this.separation = 5;
        this.hasStonerecess = false;
        this.hasStonerecess2 = false;
    }

    @Override
    protected StructureStart getStructureStart(int x, int z) {
        return new StructureMineshaftStart(x, z, random);
    }

    /**
     * Composant de couloir de mine.
     */
    public static class MineshaftCorridor extends StructureComponent {
        private final int length;
        private final int direction;

        public MineshaftCorridor(int x, int y, int z, int direction, int length) {
            super(new StructureBoundingBox(
                Math.min(x, x + length), y, Math.min(z, z + length),
                Math.max(x, x + length), y + 4, Math.max(z, z + length)
            ));
            this.length = length;
            this.direction = direction;
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            for (int i = 0; i < length; i++) {
                int localX = direction == 0 ? i : 0;
                int localZ = direction == 1 ? i : 0;

                // Sol
                placeBlock(world, BlockType.STONE, localX, 0, localZ);
                // Murs
                placeBlock(world, BlockType.COBBLESTONE, localX, 1, localZ);
                placeBlock(world, BlockType.COBBLESTONE, localX, 2, localZ);
                // Plafond
                placeBlock(world, BlockType.STONE, localX, 3, localZ);
                // Rail
                placeBlock(world, BlockType.COBBLESTONE, localX, 1, localZ);
            }
            return true;
        }
    }

    /**
     * Composant de pièce de mine.
     */
    public static class MineshaftRoom extends StructureComponent {
        private final int width;
        private final int height;
        private final int depth;

        public MineshaftRoom(int x, int y, int z, int width, int height, int depth) {
            super(new StructureBoundingBox(x, y, z, x + width, y + height, z + depth));
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            // Sol
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    placeBlock(world, BlockType.COBBLESTONE, x, 0, z);
                }
            }

            // Murs
            for (int y = 1; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    placeBlock(world, BlockType.COBBLESTONE, x, y, 0);
                    placeBlock(world, BlockType.COBBLESTONE, x, y, depth - 1);
                }
                for (int z = 0; z < depth; z++) {
                    placeBlock(world, BlockType.COBBLESTONE, 0, y, z);
                    placeBlock(world, BlockType.COBBLESTONE, width - 1, y, z);
                }
            }

            // Plafond
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    placeBlock(world, BlockType.STONE, x, height - 1, z);
                }
            }

            return true;
        }
    }
}
