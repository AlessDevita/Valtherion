package fr.alessdevita.valtherion.world.gen.structure;

import java.util.Random;

/**
 * Point de départ pour les villages.
 */
public class StructureVillageStart extends StructureStart {

    /**
     * Crée un point de départ de village.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @param random Générateur aléatoire
     */
    public StructureVillageStart(int x, int z, Random random) {
        super(x, z, 1);
        this.isStartedStructure = true;

        int y = 64; // Hauteur du terrain
        int houseCount = 3 + random.nextInt(5);

        // Routes principales
        addComponent(new MapGenVillage.VillageRoad(x, y, z, 0, 30));
        addComponent(new MapGenVillage.VillageRoad(x, y, z, 1, 30));

        // Maisons
        for (int i = 0; i < houseCount; i++) {
            int hx = x + random.nextInt(30);
            int hz = z + random.nextInt(30);
            int width = 5 + random.nextInt(4);
            int height = 4 + random.nextInt(3);
            int depth = 5 + random.nextInt(4);
            addComponent(new MapGenVillage.VillageHouse(hx, y, hz, width, height, depth));
        }

        this.isFinishedStructure = true;
    }
}
