package ru.anafro.hyperstream.leonardo.utils.math;

public final class Mod {
    private Mod() { /* util class */ }

    public static double positive(final double dividend, final double divisor) {
        return ((dividend % divisor) + divisor) % divisor;
    }
}
