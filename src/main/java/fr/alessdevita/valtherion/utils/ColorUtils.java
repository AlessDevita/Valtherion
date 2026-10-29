package fr.alessdevita.valtherion.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Classe d'utilitaires pour les couleurs dans Valtherion.
 */
public class ColorUtils {

    // Couleurs prédéfinies (RVB, 0.0 à 1.0)
    public static final Vector3f WHITE = new Vector3f(1.0f, 1.0f, 1.0f);
    public static final Vector3f BLACK = new Vector3f(0.0f, 0.0f, 0.0f);
    public static final Vector3f RED = new Vector3f(1.0f, 0.0f, 0.0f);
    public static final Vector3f GREEN = new Vector3f(0.0f, 1.0f, 0.0f);
    public static final Vector3f BLUE = new Vector3f(0.0f, 0.0f, 1.0f);
    public static final Vector3f YELLOW = new Vector3f(1.0f, 1.0f, 0.0f);
    public static final Vector3f CYAN = new Vector3f(0.0f, 1.0f, 1.0f);
    public static final Vector3f MAGENTA = new Vector3f(1.0f, 0.0f, 1.0f);
    public static final Vector3f GRAY = new Vector3f(0.5f, 0.5f, 0.5f);
    public static final Vector3f DARK_GRAY = new Vector3f(0.25f, 0.25f, 0.25f);
    public static final Vector3f LIGHT_GRAY = new Vector3f(0.75f, 0.75f, 0.75f);
    public static final Vector3f ORANGE = new Vector3f(1.0f, 0.5f, 0.0f);
    public static final Vector3f PINK = new Vector3f(1.0f, 0.5f, 0.8f);
    public static final Vector3f PURPLE = new Vector3f(0.5f, 0.0f, 0.5f);
    public static final Vector3f BROWN = new Vector3f(0.6f, 0.4f, 0.2f);

    // Couleurs prédéfinies (RVBA, 0.0 à 1.0)
    public static final Vector4f TRANSPARENT = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    /**
     * Convertit une couleur RVB en une chaîne hexadécimale.
     * @param r Composante rouge (0.0 à 1.0).
     * @param g Composante verte (0.0 à 1.0).
     * @param b Composante bleue (0.0 à 1.0).
     * @return Chaîne hexadécimale (ex: "#FF0000" pour le rouge).
     */
    public static String toHex(float r, float g, float b) {
        int red = (int) (r * 255);
        int green = (int) (g * 255);
        int blue = (int) (b * 255);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    /**
     * Convertit une couleur RVB en une chaîne hexadécimale.
     * @param color Couleur RVB.
     * @return Chaîne hexadécimale.
     */
    public static String toHex(Vector3f color) {
        return toHex(color.x, color.y, color.z);
    }

    /**
     * Convertit une couleur RVBA en une chaîne hexadécimale.
     * @param r Composante rouge (0.0 à 1.0).
     * @param g Composante verte (0.0 à 1.0).
     * @param b Composante bleue (0.0 à 1.0).
     * @param a Composante alpha (0.0 à 1.0).
     * @return Chaîne hexadécimale (ex: "#FF0000FF" pour le rouge opaque).
     */
    public static String toHex(float r, float g, float b, float a) {
        int red = (int) (r * 255);
        int green = (int) (g * 255);
        int blue = (int) (b * 255);
        int alpha = (int) (a * 255);
        return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
    }

    /**
     * Convertit une couleur RVBA en une chaîne hexadécimale.
     * @param color Couleur RVBA.
     * @return Chaîne hexadécimale.
     */
    public static String toHex(Vector4f color) {
        return toHex(color.x, color.y, color.z, color.w);
    }

    /**
     * Convertit une chaîne hexadécimale en une couleur RVB.
     * @param hex Chaîne hexadécimale (ex: "#FF0000" ou "FF0000").
     * @return Couleur RVB.
     */
    public static Vector3f fromHex(String hex) {
        if (hex == null || hex.isEmpty()) {
            return WHITE;
        }

        // Supprimer le '#' si présent
        hex = hex.replace("#", "");

        // Vérifier la longueur de la chaîne
        if (hex.length() != 3 && hex.length() != 6) {
            return WHITE;
        }

        // Convertir la chaîne hexadécimale en RVB
        int red, green, blue;
        if (hex.length() == 3) {
            red = Integer.parseInt(hex.substring(0, 1) + hex.substring(0, 1), 16);
            green = Integer.parseInt(hex.substring(1, 2) + hex.substring(1, 2), 16);
            blue = Integer.parseInt(hex.substring(2, 3) + hex.substring(2, 3), 16);
        } else {
            red = Integer.parseInt(hex.substring(0, 2), 16);
            green = Integer.parseInt(hex.substring(2, 4), 16);
            blue = Integer.parseInt(hex.substring(4, 6), 16);
        }

        return new Vector3f(red / 255.0f, green / 255.0f, blue / 255.0f);
    }

    /**
     * Convertit une chaîne hexadécimale en une couleur RVBA.
     * @param hex Chaîne hexadécimale (ex: "#FF0000FF" ou "FF0000FF").
     * @return Couleur RVBA.
     */
    public static Vector4f fromHexWithAlpha(String hex) {
        if (hex == null || hex.isEmpty()) {
            return new Vector4f(WHITE, 1.0f);
        }

        // Supprimer le '#' si présent
        hex = hex.replace("#", "");

        // Vérifier la longueur de la chaîne
        if (hex.length() != 4 && hex.length() != 8) {
            return new Vector4f(WHITE, 1.0f);
        }

        // Convertir la chaîne hexadécimale en RVBA
        int red, green, blue, alpha;
        if (hex.length() == 4) {
            red = Integer.parseInt(hex.substring(0, 1) + hex.substring(0, 1), 16);
            green = Integer.parseInt(hex.substring(1, 2) + hex.substring(1, 2), 16);
            blue = Integer.parseInt(hex.substring(2, 3) + hex.substring(2, 3), 16);
            alpha = Integer.parseInt(hex.substring(3, 4) + hex.substring(3, 4), 16);
        } else {
            red = Integer.parseInt(hex.substring(0, 2), 16);
            green = Integer.parseInt(hex.substring(2, 4), 16);
            blue = Integer.parseInt(hex.substring(4, 6), 16);
            alpha = Integer.parseInt(hex.substring(6, 8), 16);
        }

        return new Vector4f(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
    }

    /**
     * Convertit une couleur RVB en une couleur RVBA avec une opacité.
     * @param rgb Couleur RVB.
     * @param alpha Opacité (0.0 à 1.0).
     * @return Couleur RVBA.
     */
    public static Vector4f toRGBA(Vector3f rgb, float alpha) {
        return new Vector4f(rgb, alpha);
    }

    /**
     * Convertit une couleur RVBA en une couleur RVB.
     * @param rgba Couleur RVBA.
     * @return Couleur RVB.
     */
    public static Vector3f toRGB(Vector4f rgba) {
        return new Vector3f(rgba.x, rgba.y, rgba.z);
    }

    /**
     * Mélange deux couleurs RVB.
     * @param color1 Première couleur.
     * @param color2 Deuxième couleur.
     * @param t Facteur de mélange (0.0 à 1.0).
     * @return Couleur mélangée.
     */
    public static Vector3f blend(Vector3f color1, Vector3f color2, float t) {
        return new Vector3f(
                MathUtils.lerp(color1.x, color2.x, t),
                MathUtils.lerp(color1.y, color2.y, t),
                MathUtils.lerp(color1.z, color2.z, t)
        );
    }

    /**
     * Mélange deux couleurs RVBA.
     * @param color1 Première couleur.
     * @param color2 Deuxième couleur.
     * @param t Facteur de mélange (0.0 à 1.0).
     * @return Couleur mélangée.
     */
    public static Vector4f blend(Vector4f color1, Vector4f color2, float t) {
        return new Vector4f(
                MathUtils.lerp(color1.x, color2.x, t),
                MathUtils.lerp(color1.y, color2.y, t),
                MathUtils.lerp(color1.z, color2.z, t),
                MathUtils.lerp(color1.w, color2.w, t)
        );
    }

    /**
     * Éclaircit une couleur RVB.
     * @param color Couleur RVB.
     * @param factor Facteur d'éclaircissement (0.0 à 1.0).
     * @return Couleur éclaircie.
     */
    public static Vector3f lighten(Vector3f color, float factor) {
        return new Vector3f(
                MathUtils.clamp01(color.x + factor),
                MathUtils.clamp01(color.y + factor),
                MathUtils.clamp01(color.z + factor)
        );
    }

    /**
     * Assombrit une couleur RVB.
     * @param color Couleur RVB.
     * @param factor Facteur d'assombrissement (0.0 à 1.0).
     * @return Couleur assombrie.
     */
    public static Vector3f darken(Vector3f color, float factor) {
        return new Vector3f(
                MathUtils.clamp01(color.x - factor),
                MathUtils.clamp01(color.y - factor),
                MathUtils.clamp01(color.z - factor)
        );
    }

    /**
     * Inverse une couleur RVB.
     * @param color Couleur RVB.
     * @return Couleur inversée.
     */
    public static Vector3f invert(Vector3f color) {
        return new Vector3f(1.0f - color.x, 1.0f - color.y, 1.0f - color.z);
    }

    /**
     * Inverse une couleur RVBA.
     * @param color Couleur RVBA.
     * @return Couleur inversée.
     */
    public static Vector4f invert(Vector4f color) {
        return new Vector4f(1.0f - color.x, 1.0f - color.y, 1.0f - color.z, color.w);
    }

    /**
     * Convertit une couleur RVB en niveaux de gris.
     * @param color Couleur RVB.
     * @return Couleur en niveaux de gris.
     */
    public static Vector3f toGrayscale(Vector3f color) {
        float gray = (color.x + color.y + color.z) / 3.0f;
        return new Vector3f(gray, gray, gray);
    }

    /**
     * Convertit une couleur RVBA en niveaux de gris.
     * @param color Couleur RVBA.
     * @return Couleur en niveaux de gris.
     */
    public static Vector4f toGrayscale(Vector4f color) {
        float gray = (color.x + color.y + color.z) / 3.0f;
        return new Vector4f(gray, gray, gray, color.w);
    }

    /**
     * Ajoute de la transparence à une couleur RVB.
     * @param color Couleur RVB.
     * @param alpha Opacité (0.0 à 1.0).
     * @return Couleur RVBA.
     */
    public static Vector4f withAlpha(Vector3f color, float alpha) {
        return new Vector4f(color, alpha);
    }

    /**
     * Récupère la luminosité d'une couleur RVB.
     * @param color Couleur RVB.
     * @return Luminosité (0.0 à 1.0).
     */
    public static float getBrightness(Vector3f color) {
        return (color.x + color.y + color.z) / 3.0f;
    }

    /**
     * Récupère la luminosité d'une couleur RVBA.
     * @param color Couleur RVBA.
     * @return Luminosité (0.0 à 1.0).
     */
    public static float getBrightness(Vector4f color) {
        return (color.x + color.y + color.z) / 3.0f;
    }
}