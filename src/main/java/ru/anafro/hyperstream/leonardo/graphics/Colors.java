package ru.anafro.hyperstream.leonardo.graphics;

import ru.anafro.hyperstream.leonardo.utils.math.Clamp;

import java.util.function.Function;

public final class Colors {
    private static final boolean TRANSPARENCY = false;

    private Colors() { /* util class */ }

    public static int clamp(final int component) {
        return Clamp.until(component, 0xFF);
    }

    public static int a(final int argb) {
        return TRANSPARENCY ? clamp(argb >>> 24) : 0xFF;
    }

    public static int r(final int argb) {
        return clamp((argb & 0x00FF0000) >>> 16);
    }

    public static int g(final int argb) {
        return clamp((argb & 0x0000FF00) >>> 8);
    }

    public static int b(final int argb) {
        return clamp(argb & 0x000000FF);
    }

    public static int argb(final int a, final int r, final int g, final int b) {
        return (clamp(a) << 24) |
                (clamp(r) << 16) |
                (clamp(g) << 8)  |
                (clamp(b));
    }

    public static int mix(final int argbA, final int argbB, double factorA) {
        final double factorB = 1.000f - factorA;

        final int a = (int) Math.round(factorA * a(argbA) + factorB * a(argbB));
        final int r = (int) Math.round(factorA * r(argbA) + factorB * r(argbB));
        final int g = (int) Math.round(factorA * g(argbA) + factorB * g(argbB));
        final int b = (int) Math.round(factorA * b(argbA) + factorB * b(argbB));

        return argb(TRANSPARENCY ? a : 0xFF, r, g, b);
    }

    public static int saturate(int argb, double factor) {
        float[] hsl = rgbToHsl(argb);
        hsl[1] *= (float) factor;               // насыщенность
        if (hsl[1] > 1f) hsl[1] = 1f;   // clamp
        return hslToRgb(hsl, Colors.a(argb));
    }

    private static float[] rgbToHsl(int argb) {
        float r = Colors.r(argb) / 255f;
        float g = Colors.g(argb) / 255f;
        float b = Colors.b(argb) / 255f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h, s, l;
        l = (max + min) / 2f;

        if (max == min) {
            h = s = 0f; // серый
        } else {
            float d = max - min;
            s = l > 0.5f ? d / (2f - max - min) : d / (max + min);

            if (max == r) h = ((g - b) / d + (g < b ? 6f : 0f)) / 6f;
            else if (max == g) h = ((b - r) / d + 2f) / 6f;
            else h = ((r - g) / d + 4f) / 6f;
        }

        return new float[]{h, s, l};
    }

    private static int hslToRgb(float[] hsl, int alpha) {
        float h = hsl[0], s = hsl[1], l = hsl[2];
        float r, g, b;

        if (s == 0f) {
            r = g = b = l; // серый
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hue2rgb(p, q, h + 1f/3f);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1f/3f);
        }

        return Colors.argb(alpha, (int)(r*255), (int)(g*255), (int)(b*255));
    }

    private static float hue2rgb(float p, float q, float t) {
        if (t < 0f) t += 1f;
        if (t > 1f) t -= 1f;
        if (t < 1f/6f) return p + (q - p) * 6f * t;
        if (t < 1f/2f) return q;
        if (t < 2f/3f) return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }
}
