package ru.anafro.hyperstream.leonardo.generators;

import java.util.UUID;

public final class ProfilePictureGeneratorFactory {
    private ProfilePictureGeneratorFactory() { /* util class */ }

    public static AbstractProfilePictureGenerator newDefaultGenerator() {
        return new LargeMagellanicCloudProfileImageGenerator(UUID.randomUUID().toString());
    }
}
