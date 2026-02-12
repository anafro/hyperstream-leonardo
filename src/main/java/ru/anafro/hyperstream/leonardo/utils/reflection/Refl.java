package ru.anafro.hyperstream.leonardo.utils.reflection;

import static ru.anafro.hyperstream.leonardo.utils.sugar.CheckedExceptions.rethrowUnchecked;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public final class Refl {
    private Refl() {
        // util class
    }

    public static <T> T construct(Class<T> type, Object... objects) {
        final var parameterTypes = Refl.toTypeArray(objects);
        final var constructor = Refl.constructor(type, parameterTypes);

        return rethrowUnchecked(() -> constructor.newInstance(objects));
    }

    private static <T> Constructor<T> constructor(Class<T> type, Class<?>... parameterTypes) {
        return rethrowUnchecked(() -> type.getDeclaredConstructor(parameterTypes));
    }

    private static Class<?>[] toTypeArray(Object... objects) {
        return Arrays.stream(objects).map(Object::getClass).toArray(Class[]::new);
    }
}
