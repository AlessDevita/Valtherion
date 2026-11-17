package fr.alessdevita.valtherion.world.gen.layer;

import java.util.Random;

/**
 * Classe de base pour les couches de génération (GenLayer).
 * Les GenLayer sont utilisés pour générer des cartes de biomes, d'océans, de rivières, etc.
 * Chaque couche transforme une carte d'entiers en une autre carte d'entiers.
 */
public class GenLayer {

    protected final long seed;
    protected final Random random;
    protected final GenLayer parentLayer;

    /**
     * Crée une couche de génération sans parent.
     * @param seed Graine pour la génération aléatoire
     */
    public GenLayer(long seed) {
        this(seed, null);
    }

    /**
     * Crée une couche de génération avec un parent.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente dont cette couche va lire les valeurs
     */
    public GenLayer(long seed, GenLayer parentLayer) {
        this.seed = seed;
        this.random = new Random(seed);
        this.parentLayer = parentLayer;
    }

    /**
     * Génère une carte de valeurs pour la zone spécifiée.
     * @param sizeX Taille de la zone en X
     * @param sizeZ Taille de la zone en Z
     * @param startX Position de départ en X
     * @param startZ Position de départ en Z
     * @return Tableau 2D des valeurs générées
     */
    public int[] getInts(int sizeX, int sizeZ, int startX, int startZ) {
        int[] result = new int[sizeX * sizeZ];
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                result[x + z * sizeX] = this.getValue(x + startX, z + startZ);
            }
        }
        return result;
    }

    /**
     * Obtient la valeur à une position donnée.
     * À surcharger dans les sous-classes.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return La valeur à cette position
     */
    protected int getValue(int x, int z) {
        return 0;
    }

    /**
     * Initialise la couche avant la génération.
     */
    public void init() {
    }

    public long getSeed() {
        return seed;
    }

    public GenLayer getParentLayer() {
        return parentLayer;
    }
}
