package ru.anafro.hyperstream.leonardo.utils.sugar;

import java.util.function.Function;

public final class NullSafe {
    private NullSafe() { /* util class */ }

    public static <T, R> R nullish(T object, Function<T, R> actionIfPresent) {
        if (object == null) {
            return null;
        } else {
            return actionIfPresent.apply(object);
        }
    }
}
