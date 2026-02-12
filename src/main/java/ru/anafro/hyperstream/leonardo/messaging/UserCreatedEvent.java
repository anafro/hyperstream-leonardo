package ru.anafro.hyperstream.leonardo.messaging;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Delivery;

import ru.anafro.hyperstream.leonardo.utils.messaging.RabbitMQEvent;

public class UserCreatedEvent extends RabbitMQEvent {
    private String username;

    public UserCreatedEvent(Delivery delivery) {
        super(delivery);
    }

    @Override
    public void hydrate() {
        final var json = this.json();
        username = json.get("username");
    }

    public String getUsername() {
        return username;
    }

    @Override
    public byte[] serialize() {
        return this.string().getBytes(StandardCharsets.UTF_8);
    }
}
