package ru.anafro.hyperstream.leonardo.messaging;

import com.rabbitmq.client.Delivery;

import ru.anafro.hyperstream.leonardo.utils.messaging.RabbitMQEvent;

public class CreateUserEvent extends RabbitMQEvent {
    private String username;

    public CreateUserEvent(Delivery delivery) {
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
}
