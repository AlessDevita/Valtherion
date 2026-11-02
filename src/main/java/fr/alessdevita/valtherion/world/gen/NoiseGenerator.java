package fr.alessdevita.valtherion.world.gen;

import java.util.Random;

/**
 * Classe de base pour les générateurs de bruit.
 * Définit l'interface commune pour tous les types de bruit.
 */
public abstract class NoiseGenerator {

    protected final Random random;

    protected NoiseGenerator(Random random) {
        this.random = random;
    }

    /**
     * Génère une valeur de bruit en 2D.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return Valeur de bruit
     */
    public abstract double noise(double x, double z);

    /**
     * Génère une valeur de bruit en 3D.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @return Valeur de bruit
     */
    public abstract double noise(double x, double y, double z);
}
