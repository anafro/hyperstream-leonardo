package ru.anafro.hyperstream.leonardo.utils.platform;

public final class Platform {
    private Platform() { /* util class */ }

    public static String name() {
        return System.getProperty("os.name");
    }
}
