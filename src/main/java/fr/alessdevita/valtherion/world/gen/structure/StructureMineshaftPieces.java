package fr.alessdevita.valtherion.world.gen.structure;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.BlockType;

import java.util.Random;

/**
 * Pièces pour les mines souterraines.
 * Contient les différents types de composants des mines.
 */
public class StructureMineshaftPieces {

    /**
     * Pièce d'intersection de mine.
     */
    public static class Intersection extends StructureComponent {
        public Intersection(int x, int y, int z) {
            super(new StructureBoundingBox(x, y, z, x + 5, y + 5, z + 5));
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            // Creuse une pièce 5x5x5
            for (int x = 0; x < 5; x++) {
                for (int z = 0; z < 5; z++) {
                    placeBlock(world, BlockType.AIR, x, 0, z);
                    placeBlock(world, BlockType.AIR, x, 1, z);
                    placeBlock(world, BlockType.AIR, x, 2, z);
                }
            }
            return true;
        }
    }

    /**
     * Pièce de couloir droit.
     */
    public static class Straight extends StructureComponent {
        private final int length;
        private final int direction;

        public Straight(int x, int y, int z, int direction, int length) {
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
                placeBlock(world, BlockType.AIR, localX, 0, localZ);
                placeBlock(world, BlockType.AIR, localX, 1, localZ);
                placeBlock(world, BlockType.AIR, localX, 2, localZ);
            }
            return true;
        }
    }

    /**
     * Pièce de couloir tournant.
     */
    public static class Turn extends StructureComponent {
        private final int direction;

        public Turn(int x, int y, int z, int direction) {
            super(new StructureBoundingBox(x, y, z, x + 5, y + 4, z + 5));
            this.direction = direction;
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            // Creuse un couloir qui tourne
            for (int i = 0; i < 5; i++) {
                placeBlock(world, BlockType.AIR, i, 0, 0);
                placeBlock(world, BlockType.AIR, i, 1, 0);
                placeBlock(world, BlockType.AIR, i, 2, 0);
                placeBlock(world, BlockType.AIR, 4, 0, i);
                placeBlock(world, BlockType.AIR, 4, 1, i);
                placeBlock(world, BlockType.AIR, 4, 2, i);
            }
            return true;
        }
    }
}
