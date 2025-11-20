package ru.anafro.hyperstream.leonardo.graphics;

public class FlatCoordinates {
    private FlatCoordinates() { /* util class */ }

    public static int of(int x, int y, int width) {
        return x + width * y;
    }
}
