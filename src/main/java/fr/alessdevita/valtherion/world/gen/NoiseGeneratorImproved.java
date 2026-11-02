package fr.alessdevita.valtherion.world.gen;

import java.util.Random;

/**
 * Générateur de bruit amélioré avec interpolation smoothstep.
 * Version optimisée du bruit Perlin pour la génération de terrain.
 */
public class NoiseGeneratorImproved extends NoiseGenerator {

    private final long[] indices;

    public NoiseGeneratorImproved(Random random) {
        super(random);
        this.indices = new long[4096];
        for (int i = 0; i < 4096; i++) {
            indices[i] = random.nextLong();
        }
    }

    @Override
    public double noise(double x, double z) {
        double value = 0.0;
        value += x * 12.9898 + z * 78.233;
        value = Math.sin(value) * 43758.5453;
        return value - Math.floor(value);
    }

    @Override
    public double noise(double x, double y, double z) {
        return (noise(x, z) + noise(x + y, z + y)) * 0.5;
    }
}
