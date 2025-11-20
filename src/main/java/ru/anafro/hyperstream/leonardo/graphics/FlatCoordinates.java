package ru.anafro.hyperstream.leonardo.graphics;

import ru.anafro.hyperstream.leonardo.utils.math.Mod;

public class FlatCoordinates {
    private FlatCoordinates() { /* util class */ }

    public static int of(int x, int y, int width) {
        return x + width * y;
    }
}
