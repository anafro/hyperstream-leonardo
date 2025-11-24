package ru.anafro.hyperstream.leonardo.facades;

import ru.anafro.hyperstream.leonardo.configuration.Configuration;
import ru.anafro.hyperstream.leonardo.generators.ProfilePictureGeneratorFactory;

import java.awt.image.BufferedImage;

public record HyperstreamLeonardo(Configuration configuration) {
    public BufferedImage generateImage() {
        final var generator = ProfilePictureGeneratorFactory.newDefaultGenerator();
        return (BufferedImage) generator.generateImage(configuration.getProfilePictureSize());
    }
}
