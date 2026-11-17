package fr.alessdevita.valtherion.world.gen.layer;

/**
 * Cache de valeurs entières pour optimiser les accès répétés.
 * Utilisé par GenLayer pour éviter de recalculer les valeurs déjà générées.
 */
public class IntCache {

    private final int[] values;
    private final int startX;
    private final int startZ;
    private final int sizeX;
    private final int sizeZ;
    private boolean initialized;

    /**
     * Crée un cache d'entiers.
     * @param sizeX Taille du cache en X
     * @param sizeZ Taille du cache en Z
     * @param startX Position de départ en X
     * @param startZ Position de départ en Z
     */
    public IntCache(int sizeX, int sizeZ, int startX, int startZ) {
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.startX = startX;
        this.startZ = startZ;
        this.values = new int[sizeX * sizeZ];
        this.initialized = false;
    }

    /**
     * Obtient la valeur à une position donnée.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return La valeur cachée, ou 0 si hors limites
     */
    public int get(int x, int z) {
        int localX = x - startX;
        int localZ = z - startZ;

        if (localX >= 0 && localX < sizeX && localZ >= 0 && localZ < sizeZ) {
            return values[localX + localZ * sizeX];
        }

        return 0;
    }

    /**
     * Définit la valeur à une position donnée.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @param value La valeur à stocker
     */
    public void set(int x, int z, int value) {
        int localX = x - startX;
        int localZ = z - startZ;

        if (localX >= 0 && localX < sizeX && localZ >= 0 && localZ < sizeZ) {
            values[localX + localZ * sizeX] = value;
            initialized = true;
        }
    }

    /**
     * Remplit le cache avec les valeurs fournies.
     * @param ints Tableau des valeurs
     */
    public void fill(int[] ints) {
        System.arraycopy(ints, 0, values, 0, Math.min(ints.length, values.length));
        initialized = true;
    }

    /**
     * Retourne le tableau des valeurs.
     * @return Le tableau des valeurs
     */
    public int[] getValues() {
        return values;
    }

    /**
     * Vérifie si le cache a été initialisé.
     * @return true si le cache contient des données
     */
    public boolean isInitialized() {
        return initialized;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartZ() {
        return startZ;
    }
}
