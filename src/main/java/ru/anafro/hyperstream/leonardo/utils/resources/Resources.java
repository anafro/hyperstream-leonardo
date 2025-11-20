package ru.anafro.hyperstream.leonardo.utils.resources;

import ru.anafro.hyperstream.leonardo.utils.classes.Classes;

import java.io.InputStream;

public final class Resources {
    private Resources() { /* util class */ }

    public static InputStream stream(String fileName) {
        return Classes.loader().getResourceAsStream(fileName);
    }
}
