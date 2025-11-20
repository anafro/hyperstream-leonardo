package ru.anafro.hyperstream.leonardo.configuration;

import ru.anafro.hyperstream.leonardo.utils.readability.NaturalLanguage;

import java.util.Objects;

public enum ApplicationEnvironment {
    PRODUCTION("prod"),
    DEVELOPMENT("dev");

    private static final ApplicationEnvironment DEFAULT = PRODUCTION;
    private final String label;

    ApplicationEnvironment(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ApplicationEnvironment fromEnv() {
        final var env = System.getenv("environment");

        for (var environment : values()) {
            if (environment.label.equals(env)) {
                return environment;
            }
        }

        if (Objects.isNull(env)) {
            System.err.println("The 'environment' env variable is not set! It's ok, I'm defaulting to " + DEFAULT.name().toLowerCase() + "...");
        } else {
            System.err.println("I don't know a '" + env + "' environment. Choose from " +
                               NaturalLanguage.enumerateValues(ApplicationEnvironment.class) + ". " +
                               "For now, I'm defaulting to " + NaturalLanguage.name(DEFAULT) + "...");
        }

        return DEFAULT;
    }
}
