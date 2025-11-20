package ru.anafro.hyperstream.leonardo.utils.classes;

public final class Classes {
    private Classes() { /* util class */ }

    public static ClassLoader loader() {
        return Classes.class.getClassLoader();
    }
}
