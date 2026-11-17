package fr.alessdevita.valtherion.world.gen.layer;

/**
 * Couche de bordure qui détecte les transitions entre différentes valeurs.
 * Utilisée pour créer des biomes de transition entre les biomes adjacents.
 */
public class GenLayerEdge extends GenLayer {

    private static final int[][] NEIGHBOR_OFFSETS = {
        {-1, -1}, {0, -1}, {1, -1},
        {-1,  0},           {1,  0},
        {-1,  1}, {0,  1}, {1,  1}
    };

    private final int[] edgeValues;

    /**
     * Crée une couche de bordure.
     * @param seed Graine pour la génération aléatoire
     * @param parentLayer Couche parente
     * @param edgeValues Tableau des valeurs de bordure à assigner aux transitions
     */
    public GenLayerEdge(long seed, GenLayer parentLayer, int[] edgeValues) {
        super(seed, parentLayer);
        this.edgeValues = edgeValues;
    }

    @Override
    protected int getValue(int x, int z) {
        int[] centerArray = parentLayer.getInts(1, 1, x, z);
        if (centerArray == null || centerArray.length == 0) {
            return 0;
        }
        int centerValue = centerArray[0];

        // Vérifie les 8 voisins
        for (int[] offset : NEIGHBOR_OFFSETS) {
            int[] neighborArray = parentLayer.getInts(1, 1, x + offset[0], z + offset[1]);
            if (neighborArray == null || neighborArray.length == 0) {
                continue;
            }
            int neighborValue = neighborArray[0];
            if (neighborValue != centerValue) {
                // C'est une bordure
                if (edgeValues != null && edgeValues.length > 0) {
                    int edgeIndex = Math.abs(centerValue + neighborValue) % edgeValues.length;
                    return edgeValues[edgeIndex];
                }
                // Pas de valeurs de bordure, retourner la valeur centrale
                return centerValue;
            }
        }

        // Pas de bordure, retourne la valeur centrale
        return centerValue;
    }
}
