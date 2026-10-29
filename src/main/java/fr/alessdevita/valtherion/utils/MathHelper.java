package fr.alessdevita.valtherion.utils;

public class MathHelper {
    public static float cos(float v) {
        return (float) Math.cos(v);
    }

    public static float sin(float v) {
        return (float) Math.sin(v);
    }

    public static int clamp_int(int guiScale, int i, int max) {
        return Math.max(i, Math.min(guiScale, max));
    }

    public static char[] floor(float v) {
        return Integer.toString((int) Math.floor(v)).toCharArray();
    }

    public static int floor_double(double value) { return (int)Math.floor(value); }
    public static int floor_float(float value) { return (int)Math.floor(value); }
    public static float clamp_float(float value, float min, float max) { return Math.max(min, Math.min(value, max)); }
    public static double clamp_double(double value, double min, double max) { return Math.max(min, Math.min(value, max)); }
    public static int abs_int(int value) { return Math.abs(value); }
    public static float abs(float value) { return Math.abs(value); }
    public static float sqrt_float(float value) { return (float)Math.sqrt(Math.max(0.0F, value)); }
}
