package ru.anafro.hyperstream.leonardo.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.anafro.hyperstream.leonardo.http.exceptions.ImageServerRunException;
import ru.anafro.hyperstream.leonardo.facades.HyperstreamLeonardo;

import javax.imageio.ImageIO;

public final class ImageServer {
    private final HyperstreamLeonardo leonardo;
    private final int port;
    private final int maxConnections;
    private final int workerThreads;

    public ImageServer(HyperstreamLeonardo leonardo) {
        this.leonardo = leonardo;
        this.port = leonardo.configuration().getHttpPort();
        this.maxConnections = leonardo.configuration().getHttpMaxConnections();
        this.workerThreads = leonardo.configuration().getHttpWorkerThreads();
    }

    public void run() throws ImageServerRunException {
        try {
            final var address = new InetSocketAddress(this.port);
            final var server = HttpServer.create(address, maxConnections);
            final var requestHandler = new ImageRequestHandler(leonardo);
            server.setExecutor(Executors.newFixedThreadPool(workerThreads));
            server.createContext("/", requestHandler);
            server.start();
        } catch (IOException ioException) {
            throw new ImageServerRunException(ioException);
        }
    }

    protected static final class ImageRequestHandler implements HttpHandler {
        private final HyperstreamLeonardo leonardo;

        public ImageRequestHandler(HyperstreamLeonardo leonardo) {
            this.leonardo = leonardo;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            final var image = leonardo.generateImage();
            final var imageBuffer = new ByteArrayOutputStream();
            ImageIO.write(image, "png", imageBuffer);
            final var bytes = imageBuffer.toByteArray();

            final var headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "image/png");
            headers.set("Cache-Control", "no-cache");

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);

            try (final var output = exchange.getResponseBody()) {
                output.write(bytes);
            }
        }
    }
}
