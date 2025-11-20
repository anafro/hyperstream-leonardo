package ru.anafro.hyperstream.leonardo.debug;

import java.util.Objects;
import java.util.function.Function;

public final class Debug {
    private Debug() { /* util class */ }

    public static <T> T $(T any) {
        if (Objects.isNull(any)) {
            System.out.println("(None)");
        } else {
            System.out.println("-------------");
            System.out.println("A " + any.getClass().getSimpleName() + " object = ");
            System.out.println(any);
            System.out.println("-------------");
        }

        return any;
    }

    public static <T, M> T $(T any, Function<T, M> howToPrint) {
        $(howToPrint.apply(any));
        return any;
    }
}
