package ru.anafro.hyperstream.leonardo.configuration.exceptions;

import ru.anafro.hyperstream.leonardo.exceptions.HyperstreamLeonardoException;

import java.io.IOException;

public class ConfigurationLoadingException extends HyperstreamLeonardoException {
    public ConfigurationLoadingException(IOException cause) {
        super("The configuration loading has failed. See the cause exception for more details.", cause);
    }
}
