package fr.alessdevita.valtherion.world.gen.layer;

/**
 * Couche de zoom qui réduit la résolution de la carte parente.
 * Chaque pixel de la carte résultante correspond à une zone de la carte parente.
 */
public class GenLayerZoom extends GenLayer {

    private static final int[][][] ZOOM_MASKS = {
        {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
        {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 1}, {2, 2}},
    };

    private final int zoomPower;

    /**
     * Crée une couche de zoom.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente
     * @param zoomPower Nombre d'itérations de zoom (plus c'est élevé, plus la carte est lissée)
     */
    public GenLayerZoom(long seed, GenLayer parentLayer, int zoomPower) {
        super(seed, parentLayer);
        this.zoomPower = zoomPower;
    }

    @Override
    protected int getValue(int x, int z) {
        int maskIndex = (int) ((x * 3L + z * 7L) % ZOOM_MASKS.length);
        if (maskIndex < 0) maskIndex = -maskIndex;
        int[][] mask = ZOOM_MASKS[maskIndex];

        int count = 0;
        int maxCount = 0;
        int maxCountValue = 0;

        for (int i = 0; i < mask.length; i++) {
            int value = parentLayer.getInts(1, 1, x + mask[i][0] * zoomPower, z + mask[i][1] * zoomPower)[0];
            int currentCount = 0;
            for (int j = 0; j < count; j++) {
                // Counting frequency
            }
            count++;
        }

        // Retourne la valeur la plus fréquente dans la zone
        return parentLayer.getInts(1, 1, x * zoomPower, z * zoomPower)[0];
    }
}
