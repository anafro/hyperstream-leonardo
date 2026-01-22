package ru.anafro.hyperstream.leonardo.messaging;

import static ru.anafro.hyperstream.leonardo.utils.sugar.CheckedExceptions.rethrowUnchecked;

import com.rabbitmq.client.Channel;

import ru.anafro.hyperstream.leonardo.generators.ProfilePictureGenerator;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePicture;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePictureIdConverter;
import ru.anafro.hyperstream.leonardo.storage.ProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.utils.messaging.RabbitMQConnection;
import ru.anafro.hyperstream.leonardo.utils.messaging.RabbitMQEventHandler;

public class CreateUserEventHandler extends RabbitMQEventHandler<CreateUserEvent> {
    private static final String QUEUE_NAME = "user.create";
    private final Channel channel;
    private final ProfilePictureRepository repository;
    private final ProfilePictureGenerator generator;
    private final ProfilePictureIdConverter converter;

    public CreateUserEventHandler(final RabbitMQConnection connection,
            final ProfilePictureRepository repository, final ProfilePictureGenerator generator,
            final ProfilePictureIdConverter converter) {
        this.generator = generator;
        this.converter = converter;
        this.repository = repository;
        this.channel = connection.createChannel(settings -> settings
                .queue(QUEUE_NAME)
                .durable()
                .notExclusive()
                .autoDelete());
    }

    public void startAsynchronously() {
        rethrowUnchecked(() -> {
            this.channel.basicConsume(
                    QUEUE_NAME,
                    true,
                    (tag, delivery) -> handle(new CreateUserEvent(delivery)),
                    (tag) -> cancel());
        });
    }

    @Override
    public void handle(CreateUserEvent event) {
        final var pictureId = converter.convertUsernameToProfilePictureId(event.getUsername());
        final var image = generator.generateImage(pictureId);
        final var picture = new ProfilePicture(pictureId, image);
        repository.store(picture);
    }

    @Override
    public void cancel() {
        //
    }
}
