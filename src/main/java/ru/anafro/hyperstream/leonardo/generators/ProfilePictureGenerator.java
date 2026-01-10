package ru.anafro.hyperstream.leonardo.generators;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ProfilePictureGenerator {
    private final Dimension size;

    public ProfilePictureGenerator(Dimension size) {
        this.size = size;
    }

    protected abstract void draw(Dimension size, int[] canvas, int userId);

    public BufferedImage generateImage(int userId) {
        final var canvas = new int[size.width * size.height];
        final var generatedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR);

        this.draw(size, canvas, userId);
        generatedImage.setRGB(0, 0, size.width, size.height, canvas, 0, size.width);

        return generatedImage;
    }
}
