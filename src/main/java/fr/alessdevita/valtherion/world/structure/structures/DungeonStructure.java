package fr.alessdevita.valtherion.world.structure.structures;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.world.structure.Structure;

/**
 * Structure de donjon généré procéduralement.
 */
public class DungeonStructure extends Structure {
    private int mobSpawnerCount;
    private int treasureChestCount;

    /**
     * Crée une structure de donjon.
     * @param width Largeur du donjon
     * @param height Hauteur du donjon
     * @param depth Profondeur du donjon
     */
    public DungeonStructure(int width, int height, int depth) {
        super("Dungeon", width, height, depth);
        this.mobSpawnerCount = 5;
        this.treasureChestCount = 3;
    }

    /**
     * Génère le donjon dans le monde.
     * @param world Monde
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    @Override
    public void generate(World world, int x, int y, int z) {
        // Générer les murs
        for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < depth; dz++) {
                for (int dy = 0; dy < height; dy++) {
                    int px = x + dx;
                    int py = y + dy;
                    int pz = z + dz;

                    if (dx == 0 || dx == width - 1 || dz == 0 || dz == depth - 1 || dy == 0 || dy == height - 1) {
                        placeBlock(world, px, py, pz, BlockType.COBBLESTONE);
                    } else if (dy > 0 && dy < height - 1) {
                        placeBlock(world, px, py, pz, BlockType.AIR);
                    }
                }
            }
        }

        // Placer les meubles/éléments du donjon
        placeDungeonElements(world, x, y, z);
    }

    /**
     * Place les éléments du donjon (spawners, coffres).
     * @param world Monde
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    private void placeDungeonElements(World world, int x, int y, int z) {
        // Placer les spawners de mobs
        for (int i = 0; i < mobSpawnerCount; i++) {
            int dx = (i % 2) * (width - 2) + 1;
            int dz = (i / 2) * (depth - 2) + 1;
            placeBlock(world, x + dx, y + height / 2, z + dz, BlockType.GLOWSTONE);
        }

        // Placer les coffres de trésor
        for (int i = 0; i < treasureChestCount; i++) {
            int dx = (i % 2) * (width - 2) + 2;
            int dz = (i / 2) * (depth - 2) + 2;
            placeBlock(world, x + dx, y + 1, z + dz, BlockType.OBSIDIAN);
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
     * Définit le nombre de spawners de mobs.
     * @param count Nombre de spawners
     */
    public void setMobSpawnerCount(int count) {
        this.mobSpawnerCount = count;
    }

    /**
     * Définit le nombre de coffres de trésor.
     * @param count Nombre de coffres
     */
    public void setTreasureChestCount(int count) {
        this.treasureChestCount = count;
    }
}
