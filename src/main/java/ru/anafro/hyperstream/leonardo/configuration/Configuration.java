package ru.anafro.hyperstream.leonardo.configuration;

import ru.anafro.hyperstream.leonardo.configuration.exceptions.ConfigurationLoadingException;
import ru.anafro.hyperstream.leonardo.utils.resources.Resources;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static ru.anafro.hyperstream.leonardo.utils.sugar.NullSafe.nullish;

public class Configuration {
    private static final ApplicationEnvironment DEFAULT_APPLICATION_ENVIRONMENT = ApplicationEnvironment.PRODUCTION;
    private final boolean debug;
    private final int profilePictureWidth;
    private final int profilePictureHeight;
    private final Integer httpPort;
    private final Integer httpWorkerThreads;
    private final Integer httpMaxConnections;
    private final String s3Endpoint;

    public Configuration(ApplicationEnvironment applicationEnvironment) throws ConfigurationLoadingException {
        if (Objects.isNull(applicationEnvironment)) {
            System.err.println("The application environment for the configuration was not set, defaulting to " + DEFAULT_APPLICATION_ENVIRONMENT.toString().toLowerCase());
            applicationEnvironment = DEFAULT_APPLICATION_ENVIRONMENT;
        }

        try (var configurationStream = Resources.stream("configuration." + applicationEnvironment.getLabel() + ".properties")) {
            final var properties = new Properties();
            properties.load(configurationStream);
            this.debug = Boolean.parseBoolean(properties.getProperty("debug", "false"));
            this.profilePictureWidth = Integer.parseInt(properties.getProperty("pfp.width", "512"));
            this.profilePictureHeight = Integer.parseInt(properties.getProperty("pfp.height", "512"));
            this.httpPort = nullish(properties.getProperty("http.port"), Integer::parseInt);
            this.httpWorkerThreads = nullish(properties.getProperty("http.threads"), Integer::parseInt);
            this.httpMaxConnections = nullish(properties.getProperty("http.connections"), Integer::parseInt);
            this.s3Endpoint = properties.getProperty("s3.endpoint");

            Objects.requireNonNull(s3Endpoint, "Configuration files must include an Amazon S3 endpoint. Set key s3.endpoint in the configuration properties file.");
        } catch (IOException ioException) {
            throw new ConfigurationLoadingException(ioException);
        }
    }

    public boolean isDebug() {
        return this.debug;
    }

    public Dimension getProfilePictureSize() {
        return new Dimension(profilePictureWidth, profilePictureHeight);
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public Integer getHttpWorkerThreads() {
        return httpWorkerThreads;
    }

    public Integer getHttpMaxConnections() {
        return httpMaxConnections;
    }

    public String getS3Endpoint() {
        return s3Endpoint;
    }
}
