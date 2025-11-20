package ru.anafro.hyperstream.leonardo.utils.math;

public final class MathFunctions {
    private MathFunctions() { /* util class */ }

    @SuppressWarnings("unused")
    public static double staircase(final double x, final double steps, final double min, final double max) {
        final double stepSize = Math.abs(max - min) / steps;
        final long stepIndex = (long) Math.floor((x - min) * steps / max);

        return stepSize * stepIndex;
    }

    public static double map(final double x, final double min, final double max, final double newMin, final double newMax) {
        final double factor = (x - min) / (max - min);
        return factor * (newMax - newMin);
    }
}
