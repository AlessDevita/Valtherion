package fr.alessdevita.valtherion.rendering.utils;

import org.joml.*;
import org.joml.Math;

/**
 * Classe d'utilitaires mathématiques pour Valtherion.
 * Fournit des méthodes pour les interpolations, les distances, les conversions, etc.
 */
public class MathUtils {

    // Générateur de nombres aléatoires
    private static final Random random = new Random();

    // Valeur de PI
    public static final float PI = (float) Math.PI;

    // Valeur de PI * 2
    public static final float TWO_PI = (float) (2 * Math.PI);

    // Valeur de PI / 2
    public static final float HALF_PI = (float) (Math.PI / 2);

    // --- Méthodes de conversion ---

    /**
     * Convertit des degrés en radians.
     * @param degrees Degrés.
     * @return Radians.
     */
    public static float toRadians(float degrees) {
        return degrees * (PI / 180.0f);
    }

    /**
     * Convertit des radians en degrés.
     * @param radians Radians.
     * @return Degrés.
     */
    public static float toDegrees(float radians) {
        return radians * (180.0f / PI);
    }

    // --- Méthodes d'interpolation ---

    /**
     * Interpolation linéaire entre deux valeurs.
     * @param a Première valeur.
     * @param b Deuxième valeur.
     * @param t Facteur d'interpolation (0.0 à 1.0).
     * @return Valeur interpolée.
     */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    /**
     * Interpolation linéaire entre deux vecteurs.
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
     * Interpolation sphérique entre deux quaternions.
     * @param a Premier quaternion.
     * @param b Deuxième quaternion.
     * @param t Facteur d'interpolation (0.0 à 1.0).
     * @return Quaternion interpolé.
     */
    public static Quaternionf slerp(Quaternionf a, Quaternionf b, float t) {
        return new Quaternionf(a).slerp(b, t);
    }

    // --- Méthodes de distance ---

    /**
     * Calcule la distance entre deux points 2D.
     * @param x1 Coordonnée X du premier point.
     * @param y1 Coordonnée Y du premier point.
     * @param x2 Coordonnée X du deuxième point.
     * @param y2 Coordonnée Y du deuxième point.
     * @return Distance entre les deux points.
     */
    public static float distance2D(float x1, float y1, float x2, float y2) {
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
    public static float distance3D(float x1, float y1, float z1, float x2, float y2, float z2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Calcule la distance entre deux vecteurs 2D.
     * @param a Premier vecteur.
     * @param b Deuxième vecteur.
     * @return Distance entre les deux vecteurs.
     */
    public static float distance2D(Vector2f a, Vector2f b) {
        return distance2D(a.x, a.y, b.x, b.y);
    }

    /**
     * Calcule la distance entre deux vecteurs 3D.
     * @param a Premier vecteur.
     * @param b Deuxième vecteur.
     * @return Distance entre les deux vecteurs.
     */
    public static float distance3D(Vector3f a, Vector3f b) {
        return distance3D(a.x, a.y, a.z, b.x, b.y, b.z);
    }

    // --- Méthodes de nombres aléatoires ---

    /**
     * Génère un nombre aléatoire entre min et max.
     * @param min Valeur minimale.
     * @param max Valeur maximale.
     * @return Nombre aléatoire.
     */
    public static float random(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    /**
     * Génère un vecteur aléatoire dans un cube.
     * @param min Valeur minimale pour chaque composante.
     * @param max Valeur maximale pour chaque composante.
     * @return Vecteur aléatoire.
     */
    public static Vector3f randomVector3f(float min, float max) {
        return new Vector3f(
                random(min, max),
                random(min, max),
                random(min, max)
        );
    }

    /**
     * Génère un vecteur aléatoire sur une sphère.
     * @param radius Rayon de la sphère.
     * @return Vecteur aléatoire.
     */
    public static Vector3f randomVectorOnSphere(float radius) {
        float theta = random(0, TWO_PI);
        float phi = random(0, PI);

        float x = radius * (float) (Math.sin(phi) * Math.cos(theta));
        float y = radius * (float) (Math.sin(phi) * Math.sin(theta));
        float z = radius * (float) Math.cos(phi);

        return new Vector3f(x, y, z);
    }

    // --- Méthodes de calcul de normales ---

    /**
     * Calcule la normale d'un triangle.
     * @param v1 Premier vertex.
     * @param v2 Deuxième vertex.
     * @param v3 Troisième vertex.
     * @return Normale du triangle.
     */
    public static Vector3f calculateNormal(Vector3f v1, Vector3f v2, Vector3f v3) {
        Vector3f edge1 = new Vector3f(v2).sub(v1);
        Vector3f edge2 = new Vector3f(v3).sub(v1);
        return new Vector3f(edge1).cross(edge2).normalize();
    }

    /**
     * Calcule la normale moyenne d'un vertex.
     * @param vertex Vertex.
     * @param neighbors Vecteurs voisins.
     * @return Normale moyenne.
     */
    public static Vector3f calculateAverageNormal(Vector3f vertex, Vector3f... neighbors) {
        Vector3f normal = new Vector3f();
        for (int i = 0; i < neighbors.length; i += 2) {
            if (i + 2 < neighbors.length) {
                Vector3f v1 = neighbors[i];
                Vector3f v2 = neighbors[i + 1];
                Vector3f v3 = neighbors[i + 2];
                normal.add(calculateNormal(vertex, v1, v2));
                normal.add(calculateNormal(vertex, v2, v3));
            }
        }
        return normal.normalize();
    }

    // --- Méthodes de calcul de tangentes ---

    /**
     * Calcule la tangente et la bitangente d'un triangle.
     * @param v1 Premier vertex.
     * @param v2 Deuxième vertex.
     * @param v3 Troisième vertex.
     * @param uv1 Coordonnées de texture du premier vertex.
     * @param uv2 Coordonnées de texture du deuxième vertex.
     * @param uv3 Coordonnées de texture du troisième vertex.
     * @return Tableau contenant la tangente et la bitangente.
     */
    public static Vector3f[] calculateTangentAndBitangent(
            Vector3f v1, Vector3f v2, Vector3f v3,
            Vector2f uv1, Vector2f uv2, Vector2f uv3
    ) {
        Vector3f edge1 = new Vector3f(v2).sub(v1);
        Vector3f edge2 = new Vector3f(v3).sub(v1);

        Vector2f deltaUV1 = new Vector2f(uv2).sub(uv1);
        Vector2f deltaUV2 = new Vector2f(uv3).sub(uv1);

        float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

        Vector3f tangent = new Vector3f();
        tangent.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
        tangent.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
        tangent.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
        tangent.normalize();

        Vector3f bitangent = new Vector3f();
        bitangent.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
        bitangent.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
        bitangent.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
        bitangent.normalize();

        return new Vector3f[] {tangent, bitangent};
    }

    // --- Méthodes de clamp ---

    /**
     * Clamp une valeur entre min et max.
     * @param value Valeur à clamp.
     * @param min Valeur minimale.
     * @param max Valeur maximale.
     * @return Valeur clampée.
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp un vecteur entre min et max.
     * @param vector Vecteur à clamp.
     * @param min Valeur minimale pour chaque composante.
     * @param max Valeur maximale pour chaque composante.
     * @return Vecteur clampé.
     */
    public static Vector3f clamp(Vector3f vector, float min, float max) {
        return new Vector3f(
                clamp(vector.x, min, max),
                clamp(vector.y, min, max),
                clamp(vector.z, min, max)
        );
    }

    // --- Méthodes de saturation ---

    /**
     * Sature une valeur entre 0 et 1.
     * @param value Valeur à saturer.
     * @return Valeur saturée.
     */
    public static float saturate(float value) {
        return clamp(value, 0.0f, 1.0f);
    }

    /**
     * Sature un vecteur entre 0 et 1.
     * @param vector Vecteur à saturer.
     * @return Vecteur saturé.
     */
    public static Vector3f saturate(Vector3f vector) {
        return clamp(vector, 0.0f, 1.0f);
    }

    // --- Méthodes de smoothing ---

    /**
     * Fonction de smoothing (smoothstep).
     * @param edge0 Première limite.
     * @param edge1 Deuxième limite.
     * @param x Valeur à lisser.
     * @return Valeur lissée.
     */
    public static float smoothstep(float edge0, float edge1, float x) {
        float t = saturate((x - edge0) / (edge1 - edge0));
        return t * t * (3.0f - 2.0f * t);
    }

    /**
     * Fonction de smoothing plus douce (smootherstep).
     * @param edge0 Première limite.
     * @param edge1 Deuxième limite.
     * @param x Valeur à lisser.
     * @return Valeur lissée.
     */
    public static float smootherstep(float edge0, float edge1, float x) {
        float t = saturate((x - edge0) / (edge1 - edge0));
        return t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f);
    }
}