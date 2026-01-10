package ru.anafro.hyperstream.leonardo.utils.sugar;

public class Guard {
    private Guard() {
        // util class
    }

    public static void deny(boolean condition, String exceptionMessage) throws IllegalArgumentException {
        if (condition) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
