package ru.anafro.hyperstream.leonardo.utils.readability;

import java.util.Arrays;
import java.util.List;

public final class NaturalLanguage {
    private NaturalLanguage() { /* util class */ }

    public static String name(Enum<?> enumValue) {
        return enumValue.name().toLowerCase();
    }

    public static String enumerateValues(Class<? extends Enum<?>> enumClass) {
        return enumerate(Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toList());
    }

    public static String enumerate(List<String> items) {
        if (items.size() == 1) {
            return items.getFirst();
        }

        final var enumerationBuilder = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            final var item = items.get(i);


            if (i != 0) {
                enumerationBuilder.append(", ");
            }

            if (i == items.size() - 1) {
                enumerationBuilder.append(", and ");
            }

            enumerationBuilder.append(item);
        }

        return enumerationBuilder.toString();
    }
}
