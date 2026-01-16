package ru.anafro.hyperstream.leonardo;

import java.nio.file.Path;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Namespace;
import ru.anafro.hyperstream.leonardo.generators.ProfilePictureGeneratorFactory;
import ru.anafro.hyperstream.leonardo.http.ProfilePictureServer;
import ru.anafro.hyperstream.leonardo.messaging.CreateProfilePictureMessageReceiver;
import ru.anafro.hyperstream.leonardo.messaging.RabbitMQConnection;
import ru.anafro.hyperstream.leonardo.metadata.HashcodeProfilePictureIdConverter;
import ru.anafro.hyperstream.leonardo.storage.EphemeralProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.storage.FilesystemProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.storage.ProfilePictureRepository;

public class Main {
    public static void main(String[] args) {
        final var namespace = parseArgs(args);
        final var storage = namespace.getString("storage");
        final var generatorName = namespace.getString("generator");
        final var port = namespace.getInt("port");
        final var width = namespace.getInt("width");
        final var height = namespace.getInt("height");
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
            case "s3" ->
                throw new UnsupportedOperationException("S3 caching is not implemented.");
            default ->
                throw new IllegalArgumentException("'{}' storage is unknown.".formatted(storage));
        };

        final var server = new ProfilePictureServer(idConverter, repository, port);
        final var rabbitUsername = System.getenv("RABBITMQ_USER");
        final var rabbitPassword = System.getenv("RABBITMQ_PASS");
        final var rabbitConnection = new RabbitMQConnection(rabbitUsername, rabbitPassword);
        final var receiver = new CreateProfilePictureMessageReceiver(rabbitConnection, repository, generator,
                idConverter);

        receiver.startAsynchronously();
        server.start();
    }

    private static Namespace parseArgs(final String[] args) {
        final var cli = ArgumentParsers
                .newFor("HyperstreamLeonardo")
                .build()
                .defaultHelp(true)
                .description("");

        cli.addArgument("--port").type(Integer.class).required(true);
        cli.addArgument("--generator").type(String.class).required(true);
        cli.addArgument("--width").type(Integer.class).required(true);
        cli.addArgument("--height").type(Integer.class).required(true);

        final var storage = cli.addSubparsers().dest("storage");

        storage.addParser("nocache");

        final var filesystem = storage.addParser("filesystem");
        filesystem.addArgument("--path").type(String.class).required(true);

        final var s3 = storage.addParser("s3");
        s3.addArgument("--access-key").required(true);
        s3.addArgument("--secret-key").required(true);

        return cli.parseArgsOrFail(args);
    }
}
