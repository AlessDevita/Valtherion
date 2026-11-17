package fr.alessdevita.valtherion.world.gen.structure;

import java.util.Random;

/**
 * Point de départ pour les mines souterraines.
 */
public class StructureMineshaftStart extends StructureStart {

    /**
     * Crée un point de départ de mine.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @param random Générateur aléatoire
     */
    public StructureMineshaftStart(int x, int z, Random random) {
        super(x, z, 1);
        this.isStartedStructure = true;

        // Génère les composants de la mine
        int y = 40; // Hauteur souterraine

        // Couloir principal
        addComponent(new MapGenMineshaft.MineshaftCorridor(x, y, z, random.nextInt(2), 20 + random.nextInt(20)));

        // Pièce
        addComponent(new MapGenMineshaft.MineshaftRoom(x + 10, y, z + 10, 8, 6, 8));

        // Couloir secondaire
        addComponent(new MapGenMineshaft.MineshaftCorridor(x + 10, y, z + 18, random.nextInt(2), 15 + random.nextInt(15)));

        this.isFinishedStructure = true;
    }
}
