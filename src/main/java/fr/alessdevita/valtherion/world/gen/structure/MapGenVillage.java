package fr.alessdevita.valtherion.world.gen.structure;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.BlockType;

import java.util.List;
import java.util.Random;

/**
 * Générateur de villages.
 * Crée des villages avec des maisons, des routes et des fermes.
 */
public class MapGenVillage extends MapGenStructure {

    private final Random villageRandom;

    /**
     * Crée un générateur de villages.
     * @param world Monde associé
     * @param seed Graine pour la génération des villages
     */
    public MapGenVillage(World world, long seed) {
        super(world, seed);
        this.spacing = 32;
        this.separation = 4;
        this.villageRandom = new Random(seed);
    }

    @Override
    protected StructureStart getStructureStart(int x, int z) {
        return new StructureVillageStart(x, z, random);
    }

    /**
     * Composant de maison de village.
     */
    public static class VillageHouse extends StructureComponent {
        private final int width;
        private final int height;
        private final int depth;

        public VillageHouse(int x, int y, int z, int width, int height, int depth) {
            super(new StructureBoundingBox(x, y, z, x + width, y + height, z + depth));
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            // Sol en pierre
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    placeBlock(world, BlockType.COBBLESTONE, x, 0, z);
                }
            }

            // Murs en bois
            for (int y = 1; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    placeBlock(world, BlockType.WOOD, x, y, 0);
                    placeBlock(world, BlockType.WOOD, x, y, depth - 1);
                }
                for (int z = 0; z < depth; z++) {
                    placeBlock(world, BlockType.WOOD, 0, y, z);
                    placeBlock(world, BlockType.WOOD, width - 1, y, z);
                }
            }

            // Toit
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    placeBlock(world, BlockType.COBBLESTONE, x, height, z);
                }
            }

            // Porte
            placeBlock(world, BlockType.WOOD, width / 2, 1, depth - 1);
            placeBlock(world, BlockType.WOOD, width / 2, 2, depth - 1);

            return true;
        }
    }

    /**
     * Composant de route de village.
     */
    public static class VillageRoad extends StructureComponent {
        private final int length;
        private final int direction;

        public VillageRoad(int x, int y, int z, int direction, int length) {
            super(new StructureBoundingBox(
                Math.min(x, x + length), y, Math.min(z, z + length),
                Math.max(x, x + length), y + 1, Math.max(z, z + length)
            ));
            this.length = length;
            this.direction = direction;
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            for (int i = 0; i < length; i++) {
                int localX = direction == 0 ? i : 0;
                int localZ = direction == 1 ? i : 0;
                placeBlock(world, BlockType.DIRT, localX, 0, localZ);
            }
            return true;
        }
    }
}
