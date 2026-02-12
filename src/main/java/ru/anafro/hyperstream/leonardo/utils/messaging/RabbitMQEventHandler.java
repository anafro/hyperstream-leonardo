package ru.anafro.hyperstream.leonardo.utils.messaging;

public abstract class RabbitMQEventHandler<T extends RabbitMQEvent> {
    protected final RabbitMQEventBus eventBus;

    public RabbitMQEventHandler(RabbitMQEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public abstract void handle(final T event);

    public abstract void cancel();
}
