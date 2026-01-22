package ru.anafro.hyperstream.leonardo.utils.messaging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;

public abstract class RabbitMQEvent {
    protected final Delivery delivery;

    public RabbitMQEvent(Delivery delivery) {
        this.delivery = delivery;
        hydrate();
    }

    public abstract void hydrate();

    public long deliveryId() {
        return delivery.getEnvelope().getDeliveryTag();
    }

    protected byte[] bytes() {
        return delivery.getBody();
    }

    protected String string() {
        final var bytes = delivery.getBody();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    protected HashMap<String, String> json() {
        final var body = this.string();
        final var mapping = new TypeReference<HashMap<String, String>>() {
        };
        final var mapper = new ObjectMapper();

        try {
            return mapper.readValue(body, mapping);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
