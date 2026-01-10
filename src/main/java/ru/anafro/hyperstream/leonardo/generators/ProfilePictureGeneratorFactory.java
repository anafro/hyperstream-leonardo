package ru.anafro.hyperstream.leonardo.generators;

import java.awt.Dimension;
import java.util.UUID;

import ru.anafro.hyperstream.leonardo.utils.sugar.Guard;

public final class ProfilePictureGeneratorFactory {
    private ProfilePictureGeneratorFactory() {
        // util class
    }

    public static ProfilePictureGenerator create(String generatorName, int width, int height) {
        Guard.deny(width <= 0, "Width cannot be negative.");
        Guard.deny(height <= 0, "Width cannot be negative.");
        final Dimension size = new Dimension(width, height);

        return switch (generatorName.toLowerCase()) {
            case "lmc", "large-magellanic-cloud" -> new LargeMagellanicCloudProfileImageGenerator(size);
            default -> throw new IllegalArgumentException("No generator named '{}'.".formatted(generatorName));
        };
    }
}
