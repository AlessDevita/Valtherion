package fr.alessdevita.valtherion.world.gen.layer;

/**
 * Couche de lissage qui réduit les variations brusques entre les valeurs adjacentes.
 * Remplace les valeurs isolées par la valeur de leurs voisins.
 */
public class GenLayerSmooth extends GenLayer {

    private final int smoothPower;

    /**
     * Crée une couche de lissage.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente
     * @param smoothPower Nombre d'itérations de lissage
     */
    public GenLayerSmooth(long seed, GenLayer parentLayer, int smoothPower) {
        super(seed, parentLayer);
        this.smoothPower = smoothPower;
    }

    @Override
    protected int getValue(int x, int z) {
        int value = parentLayer.getInts(1, 1, x, z)[0];
        int neighborCount = 0;
        int neighborValueSum = 0;

        // Calcule la moyenne des voisins
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                int neighborValue = parentLayer.getInts(1, 1, x + dx, z + dz)[0];
                neighborValueSum += neighborValue;
                neighborCount++;
            }
        }

        int avgNeighborValue = neighborValueSum / neighborCount;

        // Si la valeur est différente de la moyenne des voisins, utilise la moyenne
        if (value != avgNeighborValue && smoothPower > 0) {
            // Vérifie si la valeur est isolée
            int sameAsCenter = 0;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (parentLayer.getInts(1, 1, x + dx, z + dz)[0] == value) {
                        sameAsCenter++;
                    }
                }
            }
            if (sameAsCenter <= 1) {
                return avgNeighborValue;
            }
        }

        return value;
    }
}
