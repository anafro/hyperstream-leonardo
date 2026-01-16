package ru.anafro.hyperstream.leonardo.messaging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

import ru.anafro.hyperstream.leonardo.generators.ProfilePictureGenerator;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePicture;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePictureIdConverter;
import ru.anafro.hyperstream.leonardo.storage.ProfilePictureRepository;

public class CreateProfilePictureMessageReceiver {
    private static final String QUEUE_NAME = "profile-picture.create";
    private final Channel channel;
    private final ProfilePictureRepository repository;
    private final ProfilePictureGenerator generator;
    private final ProfilePictureIdConverter converter;

    public CreateProfilePictureMessageReceiver(final RabbitMQConnection connection,
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
        try {
            this.channel.basicConsume(
                    QUEUE_NAME,
                    false,
                    this::onCreateProfilePictureMessage,
                    this::onCreateProfilePictureCancel);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void onCreateProfilePictureMessage(final String consumerTag, final Delivery message) {
        try {
            try {
                final var username = new String(message.getBody(), "UTF-8");
                final var pictureId = converter.convertUsernameToProfilePictureId(username);
                final var image = generator.generateImage(pictureId);
                final var picture = new ProfilePicture(pictureId, image);
                repository.store(picture);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            } catch (final UnsupportedEncodingException exception) {
                channel.basicNack(message.getEnvelope().getDeliveryTag(), false, true);
                throw new RuntimeException(exception);
            }
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void onCreateProfilePictureCancel(final String consumerTag) {
        //
    }
}
