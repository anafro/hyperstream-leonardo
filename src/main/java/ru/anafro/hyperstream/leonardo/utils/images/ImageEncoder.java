package ru.anafro.hyperstream.leonardo.utils.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import ru.anafro.hyperstream.leonardo.utils.buffering.Bytes;

public class ImageEncoder {
    private ImageEncoder() {
        // util class
    }

    public static Optional<Bytes> encodeImageToPngBytes(final BufferedImage image) {
        return encodeImage(image, "png");
    }

    private static Optional<Bytes> encodeImage(final BufferedImage image, final String encoding) {
        Objects.requireNonNull(image, "Image must be present to encode.");
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, encoding, buffer);
            return Optional.of(new Bytes(buffer.toByteArray()));
        } catch (final IOException ioException) {
            return Optional.empty();
        }

    }
}
