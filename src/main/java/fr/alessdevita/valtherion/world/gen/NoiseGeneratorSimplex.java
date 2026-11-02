package fr.alessdevita.valtherion.world.gen;

import java.util.Random;

/**
 * Générateur de bruit Simplex.
 * Alternative plus rapide et de meilleure qualité au bruit Perlin.
 */
public class NoiseGeneratorSimplex extends NoiseGenerator {

    private final int[] permutation;
    private static final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
    private static final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;

    public NoiseGeneratorSimplex(Random random) {
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
        double n0, n1, n2;
        double s = (x + z) * F2;
        int i = (int) Math.floor(x + s);
        int j = (int) Math.floor(z + s);
        double t = (i + j) * G2;
        double X0 = i - t;
        double Y0 = j - t;
        double x0 = x - X0;
        double y0 = z - Y0;

        int i1, j1;
        if (x0 > y0) { i1 = 1; j1 = 0; }
        else { i1 = 0; j1 = 1; }

        double x1 = x0 - i1 + G2;
        double y1 = y0 - j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2;
        double y2 = y0 - 1.0 + 2.0 * G2;

        int ii = i & 255;
        int jj = j & 255;
        int gi0 = permutation[ii + permutation[jj]] % 12;
        int gi1 = permutation[ii + i1 + permutation[jj + j1]] % 12;
        int gi2 = permutation[ii + 1 + permutation[jj + 1]] % 12;

        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if (t0 < 0) {
            n0 = 0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * grad(gi0, x0, y0);
        }

        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if (t1 < 0) {
            n1 = 0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * grad(gi1, x1, y1);
        }

        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0) {
            n2 = 0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * grad(gi2, x2, y2);
        }

        return 70.0 * (n0 + n1 + n2);
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise(x, z) * 0.6 + noise(y, z) * 0.4;
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 7;
        double u = h < 4 ? x : y;
        double v = h < 4 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
