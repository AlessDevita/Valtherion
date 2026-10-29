package fr.alessdevita.valtherion.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Classe d'utilitaires mathématiques pour Valtherion.
 */
public class MathUtils {

    // Constante PI
    public static final float PI = (float) Math.PI;

    // Constante PI/2
    public static final float PI_2 = PI / 2.0f;

    // Constante PI/4
    public static final float PI_4 = PI / 4.0f;

    // Constante 2*PI
    public static final float TWO_PI = 2.0f * PI;

    // Constante pour convertir les degrés en radians
    public static final float DEG_TO_RAD = PI / 180.0f;

    // Constante pour convertir les radians en degrés
    public static final float RAD_TO_DEG = 180.0f / PI;

    // Valeur minimale pour éviter les divisions par zéro
    public static final float EPSILON = 1e-6f;

    /**
     * Clamp une valeur entre un minimum et un maximum.
     * @param value Valeur à clamp.
     * @param min Valeur minimale.
     * @param max Valeur maximale.
     * @return Valeur clampée.
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp une valeur entre un minimum et un maximum.
     * @param value Valeur à clamp.
     * @param min Valeur minimale.
     * @param max Valeur maximale.
     * @return Valeur clampée.
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp une valeur entre 0 et 1.
     * @param value Valeur à clamp.
     * @return Valeur clampée entre 0 et 1.
     */
    public static float clamp01(float value) {
        return clamp(value, 0.0f, 1.0f);
    }

    /**
     * Convertit les degrés en radians.
     * @param degrees Degrés.
     * @return Radians.
     */
    public static float toRadians(float degrees) {
        return degrees * DEG_TO_RAD;
    }

    /**
     * Convertit les radians en degrés.
     * @param radians Radians.
     * @return Degrés.
     */
    public static float toDegrees(float radians) {
        return radians * RAD_TO_DEG;
    }

    /**
     * Vérifie si deux valeurs flottantes sont égales (en tenant compte de l'EPSILON).
     * @param a Première valeur.
     * @param b Deuxième valeur.
     * @return true si les valeurs sont égales, false sinon.
     */
    public static boolean equals(float a, float b) {
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Vérifie si un nombre est une puissance de deux.
     * @param number Nombre à vérifier.
     * @return true si le nombre est une puissance de deux, false sinon.
     */
    public static boolean isPowerOfTwo(int number) {
        return number > 0 && (number & (number - 1)) == 0;
    }

    /**
     * Trouve la puissance de deux supérieure ou égale à un nombre.
     * @param number Nombre.
     * @return Puissance de deux supérieure ou égale.
     */
    public static int nextPowerOfTwo(int number) {
        if (number <= 0) {
            return 1;
        }

        int power = 1;
        while (power < number) {
            power <<= 1;
        }
        return power;
    }

    /**
     * Calcule la distance entre deux points 2D.
     * @param x1 Coordonnée X du premier point.
     * @param y1 Coordonnée Y du premier point.
     * @param x2 Coordonnée X du deuxième point.
     * @param y2 Coordonnée Y du deuxième point.
     * @return Distance entre les deux points.
     */
    public static float distance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calcule la distance entre deux points 3D.
     * @param x1 Coordonnée X du premier point.
     * @param y1 Coordonnée Y du premier point.
     * @param z1 Coordonnée Z du premier point.
     * @param x2 Coordonnée X du deuxième point.
     * @param y2 Coordonnée Y du deuxième point.
     * @param z2 Coordonnée Z du deuxième point.
     * @return Distance entre les deux points.
     */
    public static float distance(float x1, float y1, float z1, float x2, float y2, float z2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Calcule la distance entre deux vecteurs 2D.
     * @param v1 Premier vecteur.
     * @param v2 Deuxième vecteur.
     * @return Distance entre les deux vecteurs.
     */
    public static float distance(Vector2f v1, Vector2f v2) {
        return distance(v1.x, v1.y, v2.x, v2.y);
    }

    /**
     * Calcule la distance entre deux vecteurs 3D.
     * @param v1 Premier vecteur.
     * @param v2 Deuxième vecteur.
     * @return Distance entre les deux vecteurs.
     */
    public static float distance(Vector3f v1, Vector3f v2) {
        return distance(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
    }

    /**
     * Calcule la distance au carré entre deux points 2D (plus rapide, pas de sqrt).
     * @param x1 Coordonnée X du premier point.
     * @param y1 Coordonnée Y du premier point.
     * @param x2 Coordonnée X du deuxième point.
     * @param y2 Coordonnée Y du deuxième point.
     * @return Distance au carré entre les deux points.
     */
    public static float distanceSquared(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }

    /**
     * Calcule la distance au carré entre deux points 3D (plus rapide, pas de sqrt).
     * @param x1 Coordonnée X du premier point.
     * @param y1 Coordonnée Y du premier point.
     * @param z1 Coordonnée Z du premier point.
     * @param x2 Coordonnée X du deuxième point.
     * @param y2 Coordonnée Y du deuxième point.
     * @param z2 Coordonnée Z du deuxième point.
     * @return Distance au carré entre les deux points.
     */
    public static float distanceSquared(float x1, float y1, float z1, float x2, float y2, float z2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Calcule le produit scalaire de deux vecteurs 2D.
     * @param x1 Coordonnée X du premier vecteur.
     * @param y1 Coordonnée Y du premier vecteur.
     * @param x2 Coordonnée X du deuxième vecteur.
     * @param y2 Coordonnée Y du deuxième vecteur.
     * @return Produit scalaire.
     */
    public static float dot(float x1, float y1, float x2, float y2) {
        return x1 * x2 + y1 * y2;
    }

    /**
     * Calcule le produit scalaire de deux vecteurs 3D.
     * @param x1 Coordonnée X du premier vecteur.
     * @param y1 Coordonnée Y du premier vecteur.
     * @param z1 Coordonnée Z du premier vecteur.
     * @param x2 Coordonnée X du deuxième vecteur.
     * @param y2 Coordonnée Y du deuxième vecteur.
     * @param z2 Coordonnée Z du deuxième vecteur.
     * @return Produit scalaire.
     */
    public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }

    /**
     * Calcule le produit scalaire de deux vecteurs 2D.
     * @param v1 Premier vecteur.
     * @param v2 Deuxième vecteur.
     * @return Produit scalaire.
     */
    public static float dot(Vector2f v1, Vector2f v2) {
        return dot(v1.x, v1.y, v2.x, v2.y);
    }

    /**
     * Calcule le produit scalaire de deux vecteurs 3D.
     * @param v1 Premier vecteur.
     * @param v2 Deuxième vecteur.
     * @return Produit scalaire.
     */
    public static float dot(Vector3f v1, Vector3f v2) {
        return dot(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
    }

    /**
     * Calcule la longueur d'un vecteur 2D.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @return Longueur du vecteur.
     */
    public static float length(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calcule la longueur d'un vecteur 3D.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @return Longueur du vecteur.
     */
    public static float length(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Calcule la longueur d'un vecteur 2D.
     * @param v Vecteur.
     * @return Longueur du vecteur.
     */
    public static float length(Vector2f v) {
        return length(v.x, v.y);
    }

    /**
     * Calcule la longueur d'un vecteur 3D.
     * @param v Vecteur.
     * @return Longueur du vecteur.
     */
    public static float length(Vector3f v) {
        return length(v.x, v.y, v.z);
    }

    /**
     * Normalise un vecteur 2D.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @return Vecteur normalisé.
     */
    public static Vector2f normalize(float x, float y) {
        float len = length(x, y);
        if (len < EPSILON) {
            return new Vector2f(0, 0);
        }
        return new Vector2f(x / len, y / len);
    }

    /**
     * Normalise un vecteur 3D.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @return Vecteur normalisé.
     */
    public static Vector3f normalize(float x, float y, float z) {
        float len = length(x, y, z);
        if (len < EPSILON) {
            return new Vector3f(0, 0, 0);
        }
        return new Vector3f(x / len, y / len, z / len);
    }

    /**
     * Normalise un vecteur 2D.
     * @param v Vecteur à normaliser.
     * @return Vecteur normalisé.
     */
    public static Vector2f normalize(Vector2f v) {
        return normalize(v.x, v.y);
    }

    /**
     * Normalise un vecteur 3D.
     * @param v Vecteur à normaliser.
     * @return Vecteur normalisé.
     */
    public static Vector3f normalize(Vector3f v) {
        return normalize(v.x, v.y, v.z);
    }

    /**
     * Calcule le produit vectoriel de deux vecteurs 3D.
     * @param x1 Coordonnée X du premier vecteur.
     * @param y1 Coordonnée Y du premier vecteur.
     * @param z1 Coordonnée Z du premier vecteur.
     * @param x2 Coordonnée X du deuxième vecteur.
     * @param y2 Coordonnée Y du deuxième vecteur.
     * @param z2 Coordonnée Z du deuxième vecteur.
     * @return Produit vectoriel.
     */
    public static Vector3f cross(float x1, float y1, float z1, float x2, float y2, float z2) {
        return new Vector3f(
                y1 * z2 - z1 * y2,
                z1 * x2 - x1 * z2,
                x1 * y2 - y1 * x2
        );
    }

    /**
     * Calcule le produit vectoriel de deux vecteurs 3D.
     * @param v1 Premier vecteur.
     * @param v2 Deuxième vecteur.
     * @return Produit vectoriel.
     */
    public static Vector3f cross(Vector3f v1, Vector3f v2) {
        return cross(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
    }

    /**
     * Lerp (interpolation linéaire) entre deux valeurs.
     * @param a Première valeur.
     * @param b Deuxième valeur.
     * @param t Facteur d'interpolation (0.0 à 1.0).
     * @return Valeur interpolée.
     */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    /**
     * Lerp (interpolation linéaire) entre deux vecteurs 2D.
     * @param a Premier vecteur.
     * @param b Deuxième vecteur.
     * @param t Facteur d'interpolation (0.0 à 1.0).
     * @return Vecteur interpolé.
     */
    public static Vector2f lerp(Vector2f a, Vector2f b, float t) {
        return new Vector2f(
                lerp(a.x, b.x, t),
                lerp(a.y, b.y, t)
        );
    }

    /**
     * Lerp (interpolation linéaire) entre deux vecteurs 3D.
     * @param a Premier vecteur.
     * @param b Deuxième vecteur.
     * @param t Facteur d'interpolation (0.0 à 1.0).
     * @return Vecteur interpolé.
     */
    public static Vector3f lerp(Vector3f a, Vector3f b, float t) {
        return new Vector3f(
                lerp(a.x, b.x, t),
                lerp(a.y, b.y, t),
                lerp(a.z, b.z, t)
        );
    }

    /**
     * Calcule le barycentre de plusieurs points 2D.
     * @param points Points 2D.
     * @return Barycentre.
     */
    public static Vector2f barycenter(Vector2f... points) {
        if (points == null || points.length == 0) {
            return new Vector2f(0, 0);
        }

        float sumX = 0;
        float sumY = 0;

        for (Vector2f point : points) {
            sumX += point.x;
            sumY += point.y;
        }

        return new Vector2f(sumX / points.length, sumY / points.length);
    }

    /**
     * Calcule le barycentre de plusieurs points 3D.
     * @param points Points 3D.
     * @return Barycentre.
     */
    public static Vector3f barycenter(Vector3f... points) {
        if (points == null || points.length == 0) {
            return new Vector3f(0, 0, 0);
        }

        float sumX = 0;
        float sumY = 0;
        float sumZ = 0;

        for (Vector3f point : points) {
            sumX += point.x;
            sumY += point.y;
            sumZ += point.z;
        }

        return new Vector3f(sumX / points.length, sumY / points.length, sumZ / points.length);
    }
}