package fr.alessdevita.valtherion.world.gen.layer;

/**
 * Couche de zoom Voronoi qui utilise un diagramme de Voronoi pour le zoom.
 * Crée des zones plus organiques et irrégulières.
 */
public class GenLayerVoronoiZoom extends GenLayer {

    private static final int[][][] ZOOM_MASKS = {
        {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
        {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 1}, {2, 2}},
    };

    private final int zoomPower;

    /**
     * Crée une couche de zoom Voronoi.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente
     * @param zoomPower Puissance du zoom
     */
    public GenLayerVoronoiZoom(long seed, GenLayer parentLayer, int zoomPower) {
        super(seed, parentLayer);
        this.zoomPower = zoomPower;
    }

    @Override
    protected int getValue(int x, int z) {
        // Utilise un hash pour déterminer le masque
        long hash = x * 3L + z * 7L;
        int maskIndex = (int) (Math.abs(hash) % ZOOM_MASKS.length);
        int[][] mask = ZOOM_MASKS[maskIndex];

        // Pour Voronoi, on utilise les coins pour déterminer la zone
        int[] cornerValues = new int[mask.length];
        for (int i = 0; i < mask.length; i++) {
            cornerValues[i] = parentLayer.getInts(1, 1,
                x + mask[i][0] * zoomPower,
                z + mask[i][1] * zoomPower)[0];
        }

        // Si tous les coins ont la même valeur, retourne cette valeur
        boolean allSame = true;
        for (int i = 1; i < cornerValues.length; i++) {
            if (cornerValues[i] != cornerValues[0]) {
                allSame = false;
                break;
            }
        }
        if (allSame) {
            return cornerValues[0];
        }

        // Sinon, utilise un hash pour choisir entre les valeurs des coins
        hash = hash * hash + hash;
        hash = hash ^ (hash >> 33);
        hash = hash * 0xff51afd7ed557ccdL;
        hash = hash ^ (hash >> 16);
        int index = (int) (Math.abs(hash) % cornerValues.length);
        return cornerValues[index];
    }
}
