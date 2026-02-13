package ru.anafro.hyperstream.leonardo;

import java.nio.file.Path;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Namespace;
import ru.anafro.hyperstream.leonardo.generators.ProfilePictureGeneratorFactory;
import ru.anafro.hyperstream.leonardo.http.ProfilePictureServer;
import ru.anafro.hyperstream.leonardo.messaging.UserCreatedEvent;
import ru.anafro.hyperstream.leonardo.messaging.UserCreatedEventHandler;
import ru.anafro.hyperstream.leonardo.metadata.HashcodeProfilePictureIdConverter;
import ru.anafro.hyperstream.leonardo.secrets.Secrets;
import ru.anafro.hyperstream.leonardo.storage.EphemeralProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.storage.FilesystemProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.storage.ProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.storage.S3ProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.utils.messaging.RabbitMQEventBus;

public class Main {
    public static void main(String[] args) {
        final var namespace = parseArgs(args);
        final var storage = namespace.getString("storage");
        final var generatorName = namespace.getString("generator");
        final var host = namespace.getString("host");
        final var port = namespace.getInt("port");
        final var width = namespace.getInt("width");
        final var height = namespace.getInt("height");
        final var needsRabbit = namespace.getBoolean("rabbit");
        final var generator = ProfilePictureGeneratorFactory.create(generatorName, width, height);
        final var idConverter = new HashcodeProfilePictureIdConverter();

        final ProfilePictureRepository repository = switch (storage) {
            case "nocache" ->
                new EphemeralProfilePictureRepository(generator);
            case "filesystem" -> {
                final var directoryPathString = namespace.getString("path");
                final var directoryPath = Path.of(directoryPathString);
                yield new FilesystemProfilePictureRepository(directoryPath);
            }
            case "s3" -> {
                final var s3Hostname = Secrets.get("S3_HOST");
                final var s3AccessKey = Secrets.get("S3_ACCESS_KEY");
                final var s3SecretKey = Secrets.get("S3_SECRET_KEY");
                final var s3Region = Secrets.get("S3_REGION");
                final var s3Bucket = Secrets.get("S3_BUCKET");
                yield new S3ProfilePictureRepository(s3Hostname, s3AccessKey, s3SecretKey, s3Region, s3Bucket);
            }
            default ->
                throw new IllegalArgumentException("'{}' storage is unknown.".formatted(storage));
        };

        if (needsRabbit) {
            final var rabbitHostname = Secrets.get("RABBITMQ_HOST");
            final var rabbitUsername = Secrets.get("RABBITMQ_USER");
            final var rabbitPassword = Secrets.get("RABBITMQ_PASS");
            final var rabbitExchange = Secrets.get("RABBITMQ_EXCHANGE");
            final var rabbitQueue = "leonardo.v1";

            final var eventBus = new RabbitMQEventBus(rabbitHostname, rabbitUsername, rabbitPassword,
                    rabbitQueue,
                    rabbitExchange);
            eventBus.registerHandler(
                    "user.created",
                    UserCreatedEvent.class,
                    new UserCreatedEventHandler(eventBus, repository, generator, idConverter));
            eventBus.startListening();
        }

        final var server = new ProfilePictureServer(idConverter, repository, host, port);
        server.start();
    }

    private static Namespace parseArgs(final String[] args) {
        final var cli = ArgumentParsers
                .newFor("HyperstreamLeonardo")
                .build()
                .defaultHelp(true)
                .description("");

        cli.addArgument("--host").type(String.class).required(true);
        cli.addArgument("--port").type(Integer.class).required(true);
        cli.addArgument("--generator").type(String.class).required(true);
        cli.addArgument("--width").type(Integer.class).required(true);
        cli.addArgument("--height").type(Integer.class).required(true);
        cli.addArgument("--rabbit").type(Boolean.class).setDefault(true);

        final var storage = cli.addSubparsers().dest("storage");

        storage.addParser("nocache");

        final var filesystem = storage.addParser("filesystem");
        filesystem.addArgument("--path").type(String.class).required(true);

        storage.addParser("s3");

        return cli.parseArgsOrFail(args);
    }
}
