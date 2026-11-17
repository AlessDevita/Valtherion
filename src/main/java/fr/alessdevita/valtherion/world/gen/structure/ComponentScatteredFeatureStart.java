package fr.alessdevita.valtherion.world.gen.structure;

import java.util.Random;

/**
 * Point de départ pour les structures dispersées (temples, ruines).
 */
public class ComponentScatteredFeatureStart extends StructureStart {

    /**
     * Crée un point de départ de structure dispersée.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @param random Générateur aléatoire
     */
    public ComponentScatteredFeatureStart(int x, int z, Random random) {
        super(x, z, 1);
        this.isStartedStructure = true;

        // Génère les composants selon le type de structure
        int y = 60; // Hauteur de surface
        int type = random.nextInt(3);

        if (type == 0) {
            // Temple désert
            addComponent(new MapGenScatteredFeature.DesertTemple(x, y, z));
        } else if (type == 1) {
            // Temple jungle
            addComponent(new MapGenScatteredFeature.DesertTemple(x, y, z));
        } else {
            // Ruines
            addComponent(new MapGenScatteredFeature.Ruins(x, y, z));
        }

        this.isFinishedStructure = true;
    }
}
