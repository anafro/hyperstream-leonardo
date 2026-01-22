package ru.anafro.hyperstream.leonardo.utils.messaging;

public abstract class RabbitMQEventHandler<T extends RabbitMQEvent> {
    public abstract void handle(final T event);

    public abstract void cancel();
}
