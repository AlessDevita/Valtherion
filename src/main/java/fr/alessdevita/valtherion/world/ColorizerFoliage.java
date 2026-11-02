package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.world.biome.BiomeType;

/**
 * Gère les couleurs de la végétation (feuilles, buissons) selon le biome et la saison.
 */
public class ColorizerFoliage {

    // Couleurs par défaut par biome (format ARGB)
    private static final int DEFAULT_FOREST = 0xFF228B22;   // Vert forêt
    private static final int DEFAULT_PLAINS = 0xFF32CD32;   // Vert lime
    private static final int DEFAULT_JUNGLE = 0xFF006400;   // Vert foncé
    private static final int DEFAULT_DESERT = 0xFF8B8000;   // Vert-jaune sec
    private static final int DEFAULT_SNOW   = 0xFFB0C4DE;   // Bleu-gris gelé
    private static final int DEFAULT_SWAMP  = 0xFF2E8B57;   // Vert mer
    private static final int DEFAULT_NETHER = 0xFF8B0000;   // Rouge sombre

    /**
     * Retourne la couleur de la végétation pour un biome donné.
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
     * Retourne la couleur de la végétation avec modulation temporelle.
     * @param biomeType Type de biome
     * @param x Coordonnée X (pour variation locale)
     * @param z Coordonnée Z (pour variation locale)
     * @return Couleur ARGB modulée
     */
    public static int getColor(BiomeType biomeType, float x, float z) {
        int base = getColor(biomeType);
        // Légère variation selon la position
        float variation = (float) ((Math.sin(x * 0.1) * Math.cos(z * 0.1)) * 0.05);
        return modulateAlpha(base, variation);
    }

    private static int modulateAlpha(int color, float factor) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = clamp((int) (red * (1 + factor)));
        green = clamp((int) (green * (1 + factor)));
        blue = clamp((int) (blue * (1 + factor)));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
