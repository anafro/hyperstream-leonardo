package ru.anafro.hyperstream.leonardo.storage;

import java.util.HashMap;
import java.util.Optional;

import ru.anafro.hyperstream.leonardo.generators.ProfilePictureGenerator;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePicture;

public final class EphemeralProfilePictureRepository extends ProfilePictureRepository {
    private final ProfilePictureGenerator generator;
    private final HashMap<Integer, ProfilePicture> storedProfilePictures;

    public EphemeralProfilePictureRepository(final ProfilePictureGenerator generator) {
        this.generator = generator;
        this.storedProfilePictures = new HashMap<>();
    }

    @Override
    public Optional<ProfilePicture> getById(int id) {
        return Optional.of(new ProfilePicture(id, generator.generateImage(id)));
    }

    @Override
    public void deleteById(int id) {
        storedProfilePictures.remove(id);
    }

    @Override
    public void store(ProfilePicture profilePicture) {
        storedProfilePictures.put(profilePicture.id(), profilePicture);
    }
}
