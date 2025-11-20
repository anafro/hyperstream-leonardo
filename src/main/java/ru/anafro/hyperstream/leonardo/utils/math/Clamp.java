package ru.anafro.hyperstream.leonardo.utils.math;

public final class Clamp {
    private Clamp() { /* util class */ }

    public static int until(final int value, final int max) {
        return Math.clamp(value, 0, max);
    }
}
