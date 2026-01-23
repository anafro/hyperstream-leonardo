package ru.anafro.hyperstream.leonardo.metadata;

import java.awt.image.BufferedImage;

import ru.anafro.hyperstream.leonardo.utils.images.ImageEncoder;

public record ProfilePicture(int id, BufferedImage image) {
    public byte[] bytes() {
        return ImageEncoder.encodeImageToPngBytes(image).orElseThrow().array();
    }
}
