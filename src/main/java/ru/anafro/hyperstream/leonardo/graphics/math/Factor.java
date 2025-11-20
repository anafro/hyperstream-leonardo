package ru.anafro.hyperstream.leonardo.graphics.math;

import ru.anafro.hyperstream.leonardo.utils.math.CubicBezier;
import ru.anafro.hyperstream.leonardo.utils.math.Mod;

public final class Factor {
    public static final float MAX = 1.000f;
    private Factor() { /* util class */ }

    public static double loop(final double coordinate) {
        return Mod.positive(coordinate, MAX);
    }

    public static double shaker(final double coordinate, CubicBezier cubicBezier) {
        double cocktailShaken;

        if ((int) coordinate % 2 == 0) {
            cocktailShaken = loop(coordinate);
        } else {
            cocktailShaken = 1 - loop(coordinate);
        }

        return cubicBezier.ease(cocktailShaken);
    }

    public static double shaker(final double coordinate) {
        return shaker(coordinate, CubicBezier.linear());
    }

    public static double of(final int value, final int max) {
        return (double) value / max;
    }
}
