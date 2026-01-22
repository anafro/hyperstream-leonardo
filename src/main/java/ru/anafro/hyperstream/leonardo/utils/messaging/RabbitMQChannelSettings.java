package ru.anafro.hyperstream.leonardo.utils.messaging;

import java.util.HashMap;

public class RabbitMQChannelSettings {
    private String queue;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;
    private HashMap<String, Object> arguments;

    private RabbitMQChannelSettings() {
        this.queue = null;
        this.durable = false;
        this.exclusive = false;
        this.autoDelete = true;
        this.arguments = new HashMap<>();
    }

    public static RabbitMQChannelSettings blank() {
        return new RabbitMQChannelSettings();
    }

    public String getQueue() {
        return queue;
    }

    public boolean isDurable() {
        return durable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public HashMap<String, Object> getArguments() {
        return arguments;
    }

    public RabbitMQChannelSettings queue(String queue) {
        this.queue = queue;
        return this;
    }

    public RabbitMQChannelSettings durable() {
        this.durable = true;
        return this;
    }

    public RabbitMQChannelSettings notDurable() {
        this.durable = false;
        return this;
    }

    public RabbitMQChannelSettings exclusive() {
        this.exclusive = true;
        return this;
    }

    public RabbitMQChannelSettings notExclusive() {
        this.exclusive = false;
        return this;

    }

    public RabbitMQChannelSettings autoDelete() {
        this.autoDelete = true;
        return this;

    }

    public RabbitMQChannelSettings manualDelete() {
        this.autoDelete = false;
        return this;

    }

    public RabbitMQChannelSettings arg(String key, Object value) {
        arguments.put(key, value);
        return this;
    }
}
