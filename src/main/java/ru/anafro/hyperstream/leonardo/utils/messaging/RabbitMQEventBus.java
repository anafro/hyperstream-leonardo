package ru.anafro.hyperstream.leonardo.utils.messaging;

import ru.anafro.hyperstream.leonardo.utils.classes.Casts;
import ru.anafro.hyperstream.leonardo.utils.reflection.Refl;
import static ru.anafro.hyperstream.leonardo.utils.sugar.CheckedExceptions.rethrowUnchecked;
import static ru.anafro.hyperstream.leonardo.utils.sugar.NullSafe.require;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

public class RabbitMQEventBus {
    private final Map<String, RabbitMQEventHandler<? extends RabbitMQEvent>> eventHandlers;
    private final Map<String, Class<? extends RabbitMQEvent>> eventTypes;
    private final Connection connection;
    private final Channel channel;
    private final String exchangeName;
    private final String queueName;

    public RabbitMQEventBus(final String host, final String username, final String password, final String queue,
            final String exchange) {
        try {
            final var connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(host);
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);
            this.eventHandlers = new HashMap<>();
            this.eventTypes = new HashMap<>();
            this.connection = connectionFactory.newConnection();
            this.channel = connection.createChannel();
            this.exchangeName = exchange;
            this.queueName = queue;

            channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(
                    queue,
                    true,
                    true,
                    false,
                    Collections.emptyMap());
        } catch (final IOException | TimeoutException exception) {
            throw new RuntimeException(exception);
        }
    }

    public <E extends RabbitMQEvent> void registerHandler(String eventName, Class<E> eventType,
            RabbitMQEventHandler<E> eventHandler) {
        eventHandlers.put(eventName, eventHandler);
        eventTypes.put(eventName, eventType);

        rethrowUnchecked(() -> {
            channel.queueBind(queueName, exchangeName, eventName);
        });
    }

    public void startListening() {
        rethrowUnchecked(() -> {
            this.channel.basicConsume(
                    queueName,
                    false,
                    this::handleDelivery,
                    (tag) -> {
                    });
        });
    }

    private void handleDelivery(final String tag, final Delivery delivery) {
        final var deliveryTag = delivery.getEnvelope().getDeliveryTag();
        final var eventName = delivery.getEnvelope().getRoutingKey();
        final var eventHandler = require(eventHandlers.get(eventName),
                "Event handler for '{}' was not registered.".formatted(eventName));
        final var eventType = eventTypes.get(eventName);
        final var event = Refl.construct(eventType, delivery);

        try {
            Casts.<RabbitMQEventHandler<RabbitMQEvent>>trustedCast(eventHandler).handle(event);
            channel.basicAck(deliveryTag, false);
        } catch (Exception exception) {
            rethrowUnchecked(() -> channel.basicNack(deliveryTag, false, true));
        }
    }
}
