package fr.alessdevita.valtherion.world.structure.structures;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.world.structure.Structure;
import java.util.Random;

/**
 * Structure de village généré procéduralement.
 */
public class VillageStructure extends Structure {
    private int buildingCount;
    private Random random;

    /**
     * Crée une structure de village.
     * @param width Largeur du village
     * @param height Hauteur du village
     * @param depth Profondeur du village
     */
    public VillageStructure(int width, int height, int depth) {
        super("Village", width, height, depth);
        this.buildingCount = 5;
        this.random = new Random();
    }

    /**
     * Génère le village dans le monde.
     * @param world Monde
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    @Override
    public void generate(World world, int x, int y, int z) {
        // Générer les bâtiments
        int buildingWidth = width / (int)Math.sqrt(buildingCount) + 1;
        int buildingDepth = depth / (int)Math.sqrt(buildingCount) + 1;
        int buildingHeight = height / 2;

        for (int i = 0; i < buildingCount; i++) {
            int bx = x + (i % (int)Math.sqrt(buildingCount)) * buildingWidth;
            int bz = z + (i / (int)Math.sqrt(buildingCount)) * buildingDepth;
            generateBuilding(world, bx, y, bz, buildingWidth, buildingHeight, buildingDepth);
        }

        // Générer une place centrale
        generatePlaza(world, x + width / 2 - 5, y, z + depth / 2 - 5, 10, 10);
    }

    /**
     * Génère un bâtiment individuel.
     * @param world Monde
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @param width Largeur du bâtiment
     * @param height Hauteur du bâtiment
     * @param depth Profondeur du bâtiment
     */
    private void generateBuilding(World world, int x, int y, int z, int width, int height, int depth) {
        // Murs du bâtiment
        BlockType wallType = random.nextBoolean() ? BlockType.WOOD : BlockType.COBBLESTONE;
        for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < depth; dz++) {
                for (int dy = 0; dy < height; dy++) {
                    if (dx == 0 || dx == width - 1 || dz == 0 || dz == depth - 1 || dy == height - 1) {
                        placeBlock(world, x + dx, y + dy, z + dz, wallType);
                    }
                }
            }
        }

        // Porte
        placeBlock(world, x + width / 2, y + 1, z, BlockType.AIR);
    }

    /**
     * Génère une place/carrefour au centre du village.
     * @param world Monde
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @param width Largeur
     * @param depth Profondeur
     */
    private void generatePlaza(World world, int x, int y, int z, int width, int depth) {
        for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < depth; dz++) {
                placeBlock(world, x + dx, y, z + dz, BlockType.GRASS);
            }
        }
    }

    /**
     * Place un bloc dans le monde.
     * @param world Monde
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @param type Type de bloc
     */
    private void placeBlock(World world, int x, int y, int z, BlockType type) {
        Block block = new Block(type) {
            @Override
            public void update(float deltaTime) {
            }
        };
        // TODO: Implémenter le placement du bloc dans le monde
    }

    /**
     * Définit le nombre de bâtiments.
     * @param count Nombre de bâtiments
     */
    public void setBuildingCount(int count) {
        this.buildingCount = count;
    }
}
