package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.example.data.MessageData;

public class RouterVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(RouterVerticle.class);

    @Override
    public void start() {
        vertx.eventBus().consumer("router", this::router);
    }

    private void router(Message<String> message) {
        if (message.body() != null && !message.body().isEmpty()) {
            logger.info("Router message: " + message.body());
            try {
                MessageData data = Json.decodeValue(message.body(), MessageData.class);
                logger.info(data);
                vertx.eventBus().send("/token/" + data.getAddress(), message.body());

                // Сохраняем сообщение в БД
                vertx.eventBus().send("database.save", message.body());
            } catch (DecodeException e) {
                logger.error("Error decode MessageData", e);
            }
        }
    }
}
