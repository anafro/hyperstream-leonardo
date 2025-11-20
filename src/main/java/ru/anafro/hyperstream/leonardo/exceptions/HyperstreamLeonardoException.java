package ru.anafro.hyperstream.leonardo.exceptions;

public class HyperstreamLeonardoException extends Exception {
    public HyperstreamLeonardoException(String message) {
        super(message);
    }

    public HyperstreamLeonardoException(String message, Throwable cause) {
        super(message, cause);
    }
}
