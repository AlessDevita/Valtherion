package fr.alessdevita.valtherion.world.gen.structure;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.BlockType;

import java.util.Random;

/**
 * Générateur de structures dispersées (temples, ruines, etc.).
 */
public class MapGenScatteredFeature extends MapGenStructure {

    private final int featureType;

    /**
     * Crée un générateur de structures dispersées.
     * @param world Monde associé
     * @param seed Graine pour la génération des structures
     * @param featureType Type de structure (0=temple désert, 1=temple jungle, 2=ruines)
     */
    public MapGenScatteredFeature(World world, long seed, int featureType) {
        super(world, seed);
        this.spacing = 32;
        this.separation = 4;
        this.featureType = featureType;
    }

    @Override
    protected StructureStart getStructureStart(int x, int z) {
        return new ComponentScatteredFeatureStart(x, z, random);
    }

    /**
     * Composant de temple désert.
     */
    public static class DesertTemple extends StructureComponent {
        public DesertTemple(int x, int y, int z) {
            super(new StructureBoundingBox(x, y, z, x + 13, y + 5, z + 13));
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            // Sol en sable
            for (int x = 0; x < 13; x++) {
                for (int z = 0; z < 13; z++) {
                    placeBlock(world, BlockType.SAND, x, 0, z);
                }
            }

            // Murs en sable
            for (int y = 1; y < 5; y++) {
                for (int x = 0; x < 13; x++) {
                    placeBlock(world, BlockType.SANDSTONE, x, y, 0);
                    placeBlock(world, BlockType.SANDSTONE, x, y, 12);
                }
                for (int z = 0; z < 13; z++) {
                    placeBlock(world, BlockType.SANDSTONE, 0, y, z);
                    placeBlock(world, BlockType.SANDSTONE, 12, y, z);
                }
            }

            return true;
        }
    }

    /**
     * Composant de ruines.
     */
    public static class Ruins extends StructureComponent {
        public Ruins(int x, int y, int z) {
            super(new StructureBoundingBox(x, y, z, x + 9, y + 4, z + 9));
        }

        @Override
        public boolean generateStructure(World world, Random random) {
            // Sol en terre
            for (int x = 0; x < 9; x++) {
                for (int z = 0; z < 9; z++) {
                    placeBlock(world, BlockType.DIRT, x, 0, z);
                }
            }

            // Murs partiellement détruits
            for (int y = 1; y < 4; y++) {
                for (int x = 0; x < 9; x++) {
                    if (random.nextBoolean()) {
                        placeBlock(world, BlockType.COBBLESTONE, x, y, 0);
                        placeBlock(world, BlockType.COBBLESTONE, x, y, 8);
                    }
                }
            }

            return true;
        }
    }
}
