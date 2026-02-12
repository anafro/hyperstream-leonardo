package ru.anafro.hyperstream.leonardo.messaging;

import ru.anafro.hyperstream.leonardo.generators.ProfilePictureGenerator;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePicture;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePictureIdConverter;
import ru.anafro.hyperstream.leonardo.storage.ProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.utils.messaging.RabbitMQEventBus;
import ru.anafro.hyperstream.leonardo.utils.messaging.RabbitMQEventHandler;

public class UserCreatedEventHandler extends RabbitMQEventHandler<UserCreatedEvent> {
    private final ProfilePictureRepository repository;
    private final ProfilePictureGenerator generator;
    private final ProfilePictureIdConverter converter;

    public UserCreatedEventHandler(final RabbitMQEventBus eventBus,
            final ProfilePictureRepository repository, final ProfilePictureGenerator generator,
            final ProfilePictureIdConverter converter) {
        super(eventBus);
        this.generator = generator;
        this.converter = converter;
        this.repository = repository;
    }

    @Override
    public void handle(UserCreatedEvent event) {
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
