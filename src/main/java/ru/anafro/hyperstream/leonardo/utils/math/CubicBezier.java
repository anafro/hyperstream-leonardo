package ru.anafro.hyperstream.leonardo.utils.math; // change as needed

public final class CubicBezier {
    private final double x1, y1, x2, y2;

    public CubicBezier(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    private double sampleCurve(double a1, double a2, double t) {
        final double invT = 1.0 - t;
        return 3.0 * invT * invT * t * a1 + 3.0 * invT * t * t * a2 + t * t * t;
    }

    private double sampleCurveX(double t) { return sampleCurve(x1, x2, t); }
    private double sampleCurveY(double t) { return sampleCurve(y1, y2, t); }

    private double sampleCurveDerivativeX(double a1, double a2, double t) {
        final double invT = 1.0 - t;
        return 3.0 * a1 * ( (1 - t) * (1 - t) ) + 6.0 * (a2 - a1) * (1 - t) * t + 3.0 * (1 - a2) * t * t;
    }

    private double sampleCurveDerivativeX(double t) {
        return 3.0 * x1 * (1 - t) * (1 - t) + 6.0 * (x2 - x1) * (1 - t) * t + 3.0 * (1 - x2) * t * t;
    }

    private double solveTForX(double x) {
        double t = x;
        for (int i = 0; i < 8; i++) {
            double xAtT = sampleCurveX(t) - x;
            double dx = sampleCurveDerivativeX(t);
            if (Math.abs(xAtT) < 1.0E-7) return t;
            if (Math.abs(dx) < 1e-6) break;
            t = t - xAtT / dx;
            if (t < 0) { t = 0; break; }
            if (t > 1) { t = 1; break; }
        }

        double t0 = 0.0;
        double t1 = 1.0;
        t = x;
        while (t0 < t1) {
            double xAtT = sampleCurveX(t);
            if (Math.abs(xAtT - x) < 1.0E-7) return t;
            if (x > xAtT) t0 = t;
            else t1 = t;
            t = (t1 + t0) * 0.5;
            if (t1 - t0 < 1e-12) return t;
        }
        return t;
    }

    public double ease(double progress) {
        if (progress <= 0) return 0;
        if (progress >= 1) return 1;
        double t = solveTForX(progress);
        return sampleCurveY(t);
    }

    public static double ease(double x1, double y1, double x2, double y2, double progress) {
        final var bezier = new CubicBezier(x1, y1, x2, y2);
        return bezier.ease(progress);
    }

    public static CubicBezier linear() { return new CubicBezier(0.0, 0.0, 1.0, 1.0); }
    public static CubicBezier ease() { return new CubicBezier(0.25, 0.1, 0.25, 1.0); }
    public static CubicBezier easeIn() { return new CubicBezier(0.42, 0.0, 1.0, 1.0); }
    public static CubicBezier easeOut() { return new CubicBezier(0.0, 0.0, 0.58, 1.0); }
    public static CubicBezier easeInOut() { return new CubicBezier(0.42, 0.0, 0.58, 1.0); }
}
