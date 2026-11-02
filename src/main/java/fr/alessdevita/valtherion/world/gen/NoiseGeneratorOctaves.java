package fr.alessdevita.valtherion.world.gen;

import java.util.Random;

/**
 * Générateur de bruit multi-octaves (fractal Brownian motion).
 * Combine plusieurs octaves de bruit pour un résultat plus naturel.
 */
public class NoiseGeneratorOctaves extends NoiseGenerator {

    private final NoiseGenerator base;
    private final int octaves;
    private final double persistence;

    public NoiseGeneratorOctaves(Random random, int octaves, double persistence) {
        super(random);
        this.base = new NoiseGeneratorPerlin(random);
        this.octaves = octaves;
        this.persistence = persistence;
    }

    @Override
    public double noise(double x, double z) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double maxValue = 0;

        for (int i = 0; i < octaves; i++) {
            total += base.noise(x * frequency, z * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2;
        }

        return total / maxValue;
    }

    @Override
    public double noise(double x, double y, double z) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double maxValue = 0;

        for (int i = 0; i < octaves; i++) {
            total += base.noise(x * frequency, y * frequency, z * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= persistence;
            frequency *= 2;
        }

        return total / maxValue;
    }
}
