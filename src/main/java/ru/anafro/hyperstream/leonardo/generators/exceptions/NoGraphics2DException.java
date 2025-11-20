package ru.anafro.hyperstream.leonardo.generators.exceptions;

import ru.anafro.hyperstream.leonardo.exceptions.HyperstreamLeonardoException;

public class NoGraphics2DException extends HyperstreamLeonardoException {
    public NoGraphics2DException() {
        super("The empty image created by a generator does not have Graphics 2D. Is Graphics 2D supported in your system?");
    }
}
