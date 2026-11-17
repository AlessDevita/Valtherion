package fr.alessdevita.valtherion.world.gen.layer;

/**
 * Couche de zoom flou qui ajoute du bruit aléatoire au zoom.
 * Crée des transitions plus naturelles entre les zones.
 */
public class GenLayerFuzzyZoom extends GenLayer {

    private static final int[][][] ZOOM_MASKS = {
        {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
        {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 1}, {2, 2}},
    };

    private final int zoomPower;

    /**
     * Crée une couche de zoom flou.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente
     * @param zoomPower Puissance du zoom
     */
    public GenLayerFuzzyZoom(long seed, GenLayer parentLayer, int zoomPower) {
        super(seed, parentLayer);
        this.zoomPower = zoomPower;
    }

    @Override
    protected int getValue(int x, int z) {
        int maskIndex = (int) ((x * 3L + z * 7L) % ZOOM_MASKS.length);
        if (maskIndex < 0) maskIndex = -maskIndex;
        int[][] mask = ZOOM_MASKS[maskIndex];

        // Compte la fréquence de chaque valeur
        int[] values = new int[mask.length];
        int[] counts = new int[mask.length];
        int uniqueCount = 0;

        for (int i = 0; i < mask.length; i++) {
            int value = parentLayer.getInts(1, 1, x + mask[i][0] * zoomPower, z + mask[i][1] * zoomPower)[0];
            int found = -1;
            for (int j = 0; j < uniqueCount; j++) {
                if (values[j] == value) {
                    found = j;
                    break;
                }
            }
            if (found >= 0) {
                counts[found]++;
            } else {
                values[uniqueCount] = value;
                counts[uniqueCount] = 1;
                uniqueCount++;
            }
        }

        // Trouve la valeur la plus fréquente
        int maxCount = 0;
        int maxCountValue = values[0];
        for (int i = 0; i < uniqueCount; i++) {
            if (counts[i] > maxCount) {
                maxCount = counts[i];
                maxCountValue = values[i];
            }
        }

        // Si plusieurs valeurs ont la même fréquence, choisit aléatoirement
        if (uniqueCount > 1) {
            int sameCount = 0;
            for (int i = 0; i < uniqueCount; i++) {
                if (counts[i] == maxCount) sameCount++;
            }
            if (sameCount > 1 && random.nextBoolean()) {
                // Retourne une valeur aléatoire parmi les plus fréquentes
                for (int i = 0; i < uniqueCount; i++) {
                    if (counts[i] == maxCount) {
                        if (random.nextBoolean()) return values[i];
                    }
                }
            }
        }

        return maxCountValue;
    }
}
