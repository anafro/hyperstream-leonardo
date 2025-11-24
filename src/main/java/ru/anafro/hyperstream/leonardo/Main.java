package ru.anafro.hyperstream.leonardo;

import ru.anafro.hyperstream.leonardo.configuration.ApplicationEnvironment;
import ru.anafro.hyperstream.leonardo.configuration.Configuration;
import ru.anafro.hyperstream.leonardo.configuration.exceptions.ConfigurationLoadingException;
import ru.anafro.hyperstream.leonardo.debug.ImageViewer;
import ru.anafro.hyperstream.leonardo.debug.exceptions.ImageServerRunException;
import ru.anafro.hyperstream.leonardo.facades.HyperstreamLeonardo;
import ru.anafro.hyperstream.leonardo.http.ImageServer;

public class Main {
    public static void main(String[] args) throws ConfigurationLoadingException, ImageServerRunException, InterruptedException {
        if (args.length >= 2) {
            System.err.println("You provided some arguments, but we do not need any! " +
                               "It's ok, we'll just ignore them, going on...");
        }

        final var environment = ApplicationEnvironment.fromEnv();
        final var configuration = new Configuration(environment);
        final var leonardo = new HyperstreamLeonardo(configuration);
        final var image = leonardo.generateImage();

        if (configuration.isDebug()) {
            final var viewer = new ImageViewer();
            viewer.show(image);

            final var server = new ImageServer(leonardo);
            server.run();
            Thread.currentThread().join();
        }
    }
}
