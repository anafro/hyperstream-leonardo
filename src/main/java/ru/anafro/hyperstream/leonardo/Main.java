package ru.anafro.hyperstream.leonardo;

import ru.anafro.hyperstream.leonardo.configuration.ApplicationEnvironment;
import ru.anafro.hyperstream.leonardo.configuration.Configuration;
import ru.anafro.hyperstream.leonardo.configuration.exceptions.ConfigurationLoadingException;
import ru.anafro.hyperstream.leonardo.debug.ImageViewer;
import ru.anafro.hyperstream.leonardo.generators.LargeMagellanicCloudProfileImageGenerator;

import java.awt.*;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws ConfigurationLoadingException {
        if (args.length >= 2) {
            System.err.println("You provided some arguments, but we do not need any! " +
                               "It's ok, we'll just ignore them, going on...");
        }

        final var configuration = new Configuration(ApplicationEnvironment.DEVELOPMENT);
        final var generator = new LargeMagellanicCloudProfileImageGenerator(UUID.randomUUID().toString());
        final var image = generator.generateImage(new Dimension(1024, 1024));

        if (configuration.isDebug()) {
            final var viewer = new ImageViewer();
            viewer.show(image);
        }
    }
}
