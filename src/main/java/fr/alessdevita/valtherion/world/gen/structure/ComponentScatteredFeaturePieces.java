package fr.alessdevita.valtherion.world.gen.structure;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.BlockType;

import java.util.Random;

/**
 * Composants pour les structures dispersées.
 */
public class ComponentScatteredFeaturePieces {

    /**
     * Point de départ pour les structures dispersées.
     */
    public static class ComponentScatteredFeatureStart extends StructureStart {
        public ComponentScatteredFeatureStart(int x, int z, Random random) {
            super(x, z, 1);
            this.isStartedStructure = true;

            int y = 64;
            int type = random.nextInt(3);

            if (type == 0) {
                // Temple désert
                addComponent(new DesertTemple(x, y, z));
            } else if (type == 1) {
                // Temple jungle
                addComponent(new JungleTemple(x, y, z));
            } else {
                // Ruines
                addComponent(new Ruins(x, y, z));
            }

            this.isFinishedStructure = true;
        }
    }

    /**
     * Temple désert.
     */
    public static class DesertTemple extends StructureComponent {
        public DesertTemple(int x, int y, int z) {
            super(new StructureBoundingBox(x, y, z, x + 13, y + 5, z + 13));
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            for (int lx = 0; lx < 13; lx++) {
                for (int lz = 0; lz < 13; lz++) {
                    placeBlock(world, BlockType.SAND, lx, 0, lz);
                }
            }
            for (int ly = 1; ly < 5; ly++) {
                for (int lx = 0; lx < 13; lx++) {
                    placeBlock(world, BlockType.SANDSTONE, lx, ly, 0);
                    placeBlock(world, BlockType.SANDSTONE, lx, ly, 12);
                }
                for (int lz = 0; lz < 13; lz++) {
                    placeBlock(world, BlockType.SANDSTONE, 0, ly, lz);
                    placeBlock(world, BlockType.SANDSTONE, 12, ly, lz);
                }
            }
            return true;
        }
    }

    /**
     * Temple jungle.
     */
    public static class JungleTemple extends StructureComponent {
        public JungleTemple(int x, int y, int z) {
            super(new StructureBoundingBox(x, y, z, x + 15, y + 11, z + 15));
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            for (int lx = 0; lx < 15; lx++) {
                for (int lz = 0; lz < 15; lz++) {
                    placeBlock(world, BlockType.DIRT, lx, 0, lz);
                }
            }
            for (int ly = 1; ly < 11; ly++) {
                for (int lx = 0; lx < 15; lx++) {
                    placeBlock(world, BlockType.COBBLESTONE, lx, ly, 0);
                    placeBlock(world, BlockType.COBBLESTONE, lx, ly, 14);
                }
                for (int lz = 0; lz < 15; lz++) {
                    placeBlock(world, BlockType.COBBLESTONE, 0, ly, lz);
                    placeBlock(world, BlockType.COBBLESTONE, 14, ly, lz);
                }
            }
            return true;
        }
    }

    /**
     * Ruines.
     */
    public static class Ruins extends StructureComponent {
        public Ruins(int x, int y, int z) {
            super(new StructureBoundingBox(x, y, z, x + 9, y + 4, z + 9));
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            for (int lx = 0; lx < 9; lx++) {
                for (int lz = 0; lz < 9; lz++) {
                    placeBlock(world, BlockType.DIRT, lx, 0, lz);
                }
            }
            for (int ly = 1; ly < 4; ly++) {
                for (int lx = 0; lx < 9; lx++) {
                    if (random.nextBoolean()) {
                        placeBlock(world, BlockType.COBBLESTONE, lx, ly, 0);
                        placeBlock(world, BlockType.COBBLESTONE, lx, ly, 8);
                    }
                }
            }
            return true;
        }
    }
}
