package ru.anafro.hyperstream.leonardo.utils.math;

public final class Clamp {
    private Clamp() { /* util class */ }

    public static float until(final float value, final float max) {
        return Math.clamp(value, 0.000f, max);
    }

    public static int until(final int value, final int max) {
        return Math.clamp(value, 0, max);
    }
}
