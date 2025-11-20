package ru.anafro.hyperstream.leonardo.configuration;

import ru.anafro.hyperstream.leonardo.configuration.exceptions.ConfigurationLoadingException;
import ru.anafro.hyperstream.leonardo.utils.resources.Resources;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Configuration {
    private static final ApplicationEnvironment DEFAULT_APPLICATION_ENVIRONMENT = ApplicationEnvironment.PRODUCTION;
    private final boolean debug;

    public Configuration(ApplicationEnvironment applicationEnvironment) throws ConfigurationLoadingException {
        if (Objects.isNull(applicationEnvironment)) {
            System.err.println("The application environment for the configuration was not set, defaulting to " + DEFAULT_APPLICATION_ENVIRONMENT.toString().toLowerCase());
            applicationEnvironment = DEFAULT_APPLICATION_ENVIRONMENT;
        }

        try (var configurationStream = Resources.stream("configuration." + applicationEnvironment.getLabel() + ".properties")) {
            final var properties = new Properties();
            properties.load(configurationStream);
            this.debug = Boolean.parseBoolean(properties.getProperty("debug", "false"));
        } catch (IOException ioException) {
            throw new ConfigurationLoadingException(ioException);
        }
    }

    public boolean isDebug() {
        return this.debug;
    }
}
