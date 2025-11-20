package ru.anafro.hyperstream.leonardo.generators;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ru.anafro.hyperstream.leonardo.debug.Debug.$;

public abstract class AbstractProfilePictureGenerator {
    protected abstract void draw(Dimension size, int[] canvas);
    public Image generateImage(Dimension size) {
        final var canvas = new int[size.width * size.height];
        final var generatedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR);

        this.draw(size, canvas);
        generatedImage.setRGB(0, 0, size.width, size.height, canvas, 0, size.width);

        return generatedImage;
    }
}
