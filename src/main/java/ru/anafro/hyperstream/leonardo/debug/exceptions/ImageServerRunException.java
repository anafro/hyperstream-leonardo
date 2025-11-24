package ru.anafro.hyperstream.leonardo.debug.exceptions;

import ru.anafro.hyperstream.leonardo.exceptions.HyperstreamLeonardoException;

import java.io.IOException;

public class ImageServerRunException extends HyperstreamLeonardoException {
    public ImageServerRunException(IOException cause) {
        super("Image server failed to run. See the IOException trace for details.", cause);
    }
}
