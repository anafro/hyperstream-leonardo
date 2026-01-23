package ru.anafro.hyperstream.leonardo.utils.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

public class ImageDecoder {
    private ImageDecoder() {
        // util class
    }

    public static Optional<BufferedImage> decodePngBytesToImage(final byte[] bytes) {
        return decodeImage(bytes, "png");
    }

    private static Optional<BufferedImage> decodeImage(final byte[] bytes, final String encoding) {
        Objects.requireNonNull(bytes, "Bytes must be present to encode.");
        final ByteArrayInputStream buffer = new ByteArrayInputStream(bytes);

        try {
            final var image = ImageIO.read(buffer);
            return Optional.of(image);
        } catch (final IOException ioException) {
            return Optional.empty();
        }
    }
}
