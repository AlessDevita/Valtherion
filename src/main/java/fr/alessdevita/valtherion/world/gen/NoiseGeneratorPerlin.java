package fr.alessdevita.valtherion.world.gen;

import java.util.Random;

/**
 * Générateur de bruit Perlin classique.
 * Produit un bruit continu et naturel pour la génération de terrain.
 */
public class NoiseGeneratorPerlin extends NoiseGenerator {

    private final int[] permutation;

    public NoiseGeneratorPerlin(Random random) {
        super(random);
        this.permutation = new int[512];
        initPermutation();
    }

    private void initPermutation() {
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) p[i] = i;
        for (int i = 255; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = p[i]; p[i] = p[j]; p[j] = temp;
        }
        for (int i = 0; i < 512; i++) permutation[i] = p[i & 255];
    }

    @Override
    public double noise(double x, double z) {
        return noise2D(x, z);
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise3D(x, y, z);
    }

    private double noise2D(double x, double z) {
        int xi = (int) Math.floor(x) & 255;
        int zi = (int) Math.floor(z) & 255;
        double xf = x - Math.floor(x);
        double zf = z - Math.floor(z);

        double u = fade(xf);
        double v = fade(zf);

        int aa = permutation[permutation[xi] + zi];
        int ab = permutation[permutation[xi] + zi + 1];
        int ba = permutation[permutation[xi + 1] + zi];
        int bb = permutation[permutation[xi + 1] + zi + 1];

        double x1 = grad(aa, xf, zf);
        double x2 = grad(ba, xf - 1, zf);
        double y1 = lerp(u, x1, x2);
        double x3 = grad(ab, xf, zf - 1);
        double x4 = grad(bb, xf - 1, zf - 1);
        double y2 = lerp(u, x3, x4);

        return lerp(v, y1, y2);
    }

    private double noise3D(double x, double y, double z) {
        return noise2D(x, z) * 0.7 + noise2D(y, z) * 0.3;
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double z) {
        int h = hash & 3;
        return ((h & 1) == 0 ? x : -x) + ((h & 2) == 0 ? z : -z);
    }
}
