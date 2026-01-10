package ru.anafro.hyperstream.leonardo.storage;

import java.util.Optional;

import ru.anafro.hyperstream.leonardo.metadata.ProfilePicture;

public abstract class ProfilePictureRepository {
    public abstract Optional<ProfilePicture> getById(final int id);

    public abstract void deleteById(final int id);

    public abstract void store(final ProfilePicture profilePicture);
}
