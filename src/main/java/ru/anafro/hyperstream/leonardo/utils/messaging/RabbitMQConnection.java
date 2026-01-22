package ru.anafro.hyperstream.leonardo.utils.messaging;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConnection {
    private final Connection connection;

    public RabbitMQConnection(final String host, final String username, final String password) {
        try {
            final var connectionFactory = new ConnectionFactory();
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);
            this.connection = connectionFactory.newConnection();
        } catch (final IOException | TimeoutException exception) {
            throw new RuntimeException(exception);
        }
    }

    public RabbitMQConnection(final String username, final String password) {
        this("localhost", username, password);
    }

    public Channel createChannel(Consumer<RabbitMQChannelSettings> setupChannel) {
        try {
            final var channel = connection.createChannel();
            final var settings = RabbitMQChannelSettings.blank();
            setupChannel.accept(settings);

            channel.queueDeclare(
                    settings.getQueue(),
                    settings.isDurable(),
                    settings.isExclusive(),
                    settings.isAutoDelete(),
                    settings.getArguments());

            return channel;
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
