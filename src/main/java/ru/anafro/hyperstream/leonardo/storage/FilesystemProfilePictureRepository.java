package ru.anafro.hyperstream.leonardo.storage;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.imageio.ImageIO;

import ru.anafro.hyperstream.leonardo.metadata.ProfilePicture;

public class FilesystemProfilePictureRepository extends ProfilePictureRepository {
    private Path directoryPath;

    public FilesystemProfilePictureRepository(final Path directoryPath) {
        this.directoryPath = directoryPath;

        if (Files.notExists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (final IOException ioException) {
                throw new IllegalArgumentException("Creating '{}' failed.", ioException);
            }
        }

        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("'{}' is not a directory.".formatted(directoryPath));
        }

        if (!Files.isWritable(directoryPath)) {
            throw new IllegalArgumentException(
                    "Leonardo does not have priveleges to add files to '{}'.".formatted(directoryPath));
        }
    }

    @Override
    public Optional<ProfilePicture> getById(int id) {
        final Path profilePicturePath = this.resolveProfilePictureImagePath(id);

        if (Files.notExists(profilePicturePath)) {
            return Optional.empty();
        }

        try {
            final BufferedImage profilePictureImage = ImageIO.read(profilePicturePath.toFile());
            final ProfilePicture profilePicture = new ProfilePicture(id, profilePictureImage);
            return Optional.of(profilePicture);
        } catch (final IOException ioException) {
            throw new IllegalArgumentException("'{}' cannot be read.", ioException);
        }
    }

    @Override
    public void deleteById(int id) {
        final Path profilePicturePath = this.resolveProfilePictureImagePath(id);
        try {
            Files.deleteIfExists(profilePicturePath);
        } catch (final IOException ioException) {
            throw new IllegalArgumentException("'{}' cannot be deleted.", ioException);
        }
    }

    @Override
    public void store(ProfilePicture profilePicture) {
        final Path profilePicturePath = this.resolveProfilePictureImagePath(profilePicture.id());

        if (Files.exists(profilePicturePath)) {
            this.deleteById(profilePicture.id());
        }

        try {
            ImageIO.write(profilePicture.image(), "png", new FileOutputStream(profilePicturePath.toFile()));
        } catch (final IOException ioException) {
            throw new IllegalArgumentException("'{}' cannot be written.", ioException);
        }
    }

    private Path resolveProfilePictureImagePath(int id) {
        return this.directoryPath.resolve("%d.png".formatted(id));
    }
}
