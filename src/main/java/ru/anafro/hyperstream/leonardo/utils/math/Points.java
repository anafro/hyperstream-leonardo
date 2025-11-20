package ru.anafro.hyperstream.leonardo.utils.math;

public final class Points {
    private Points() { /* util class */ }

    public static double rotateX(final double x, final double y, final double cx, final double cy, final double alpha) {
        return cx + (x - cx) * Math.cos(alpha) - (y - cy) * Math.sin(alpha);
    }

    public static double rotateY(final double x, final double y, final double cx, final double cy, final double alpha) {
        return cy + (x - cx) * Math.sin(alpha) + (y - cy) * Math.cos(alpha);
    }

    public static double distance(final double ax, final double ay, final double bx, final double by) {
        return Math.sqrt(Math.pow(ax - bx, 2) + Math.pow(ay - by, 2));
    }
}
