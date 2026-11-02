package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Gère les couleurs de l'herbe selon le biome, la température et l'humidité.
 */
public class ColorizerGrass {

    // Couleurs par défaut par biome (format ARGB)
    private static final int DEFAULT_FOREST = 0xFF4CAF50;   // Vert standard
    private static final int DEFAULT_PLAINS = 0xFF8BC34A;   // Vert clair
    private static final int DEFAULT_JUNGLE = 0xFF2E7D32;   // Vert foncé
    private static final int DEFAULT_DESERT = 0xFFCDDC39;   // Jaune sec
    private static final int DEFAULT_SNOW   = 0xFFE0E0E0;   // Blanc-gris
    private static final int DEFAULT_SWAMP  = 0xFF558B2F;   // Vert olive
    private static final int DEFAULT_NETHER = 0xFF5D4037;   // Brun terre

    /**
     * Retourne la couleur de l'herbe pour un biome donné.
     * @param biomeType Type de biome
     * @return Couleur ARGB
     */
    public static int getColor(BiomeType biomeType) {
        if (biomeType == null) return DEFAULT_FOREST;
        switch (biomeType) {
            case FOREST:  return DEFAULT_FOREST;
            case PLAINS:  return DEFAULT_PLAINS;
            case JUNGLE:  return DEFAULT_JUNGLE;
            case DESERT:  return DEFAULT_DESERT;
            case SNOW:    return DEFAULT_SNOW;
            case SWAMP:   return DEFAULT_SWAMP;
            case NETHER:  return DEFAULT_NETHER;
            default:      return DEFAULT_FOREST;
        }
    }

    /**
     * Retourne la couleur de l'herbe avec modulation selon température et humidité.
     * @param biomeType Type de biome
     * @param temperature Température (0.0 = froid, 1.0 = chaud)
     * @param humidity Humidité (0.0 = sec, 1.0 = humide)
     * @return Couleur ARGB modulée
     */
    public static int getColor(BiomeType biomeType, float temperature, float humidity) {
        int base = getColor(biomeType);

        // La température influence le jaune/brun
        // L'humidité influence le vert/brun
        float redFactor = 1.0f - humidity * 0.3f + temperature * 0.2f;
        float greenFactor = humidity * 0.5f + 0.5f;
        float blueFactor = 1.0f - temperature * 0.3f;

        return modulateColor(base, redFactor, greenFactor, blueFactor);
    }

    /**
     * Retourne la couleur de l'herbe avec variation spatiale.
     * @param biomeType Type de biome
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return Couleur ARGB
     */
    public static int getColorWithVariation(BiomeType biomeType, float x, float z) {
        int base = getColor(biomeType);
        float variation = (float) ((Math.sin(x * 0.05) * Math.cos(z * 0.05)) * 0.1);
        return modulateColor(base, 1.0f + variation, 1.0f + variation, 1.0f);
    }

    private static int modulateColor(int color, float redFactor, float greenFactor, float blueFactor) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = clamp((int) (red * redFactor));
        green = clamp((int) (green * greenFactor));
        blue = clamp((int) (blue * blueFactor));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
