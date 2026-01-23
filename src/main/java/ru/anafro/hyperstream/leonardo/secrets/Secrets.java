package ru.anafro.hyperstream.leonardo.secrets;

import java.nio.file.Files;
import java.nio.file.Path;

import static ru.anafro.hyperstream.leonardo.utils.sugar.CheckedExceptions.rethrowUnchecked;
import ru.anafro.hyperstream.leonardo.utils.sugar.Guard;

public final class Secrets {
    private Secrets() {
        // util class
    }

    public static String get(String key) {
        System.out.println(System.getenv().toString());
        final var fileKey = key + "_FILE";

        Guard.deny(!key.matches("^[A-Z_]{1,}$"), "Secret key must be in SCREAMING_CASE, not: " + key);

        final var secretEnv = System.getenv(key);
        final var secretFileEnv = System.getenv(fileKey);

        Guard.deny(secretEnv == null && secretFileEnv == null,
                "Neither %s, nor %s was provided, provide exactly one.".formatted(key, fileKey));
        Guard.deny(secretEnv != null && secretFileEnv != null,
                "Both %s, and %s was provided, provide only one.".formatted(key, fileKey));

        if (secretEnv != null) {
            return secretEnv;
        } else {
            final var secretFilepath = Path.of(secretFileEnv);
            Guard.deny(Files.notExists(secretFilepath),
                    "The secret file '%s' does not exist.".formatted(secretFilepath));

            return rethrowUnchecked(() -> Files.readString(secretFilepath));
        }
    }
}
