package ru.anafro.hyperstream.leonardo.utils.classes;

public final class Casts {
    private Casts() {
        // util class
    }

    @SuppressWarnings("unchecked")
    public static <T> T trustedCast(Object object) {
        return (T) object;
    }
}
