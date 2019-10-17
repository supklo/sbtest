package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

public class MongoDbVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(MongoDbVerticle.class);

    private final MongoClient client;

    public MongoDbVerticle(MongoClient client) {
        this.client = client;
    }

    @Override
    public void start() {
        vertx.eventBus().consumer("database.save", this::saveDb);
        vertx.eventBus().consumer("getHistory", this::getHistory);
        vertx.eventBus().consumer("images.save", this::saveImage);
        vertx.eventBus().consumer("images.findImageByUUID", this::findFileByUUID);
    }

    private void getHistory(Message<String> message) {
        client.find("message", new JsonObject(),
                result -> message.reply(Json.encode(result.result()))
        );
    }

    private void saveDb(Message<String> message) {
        client.insert("message", new JsonObject(message.body()), this::handler);
    }

    private void saveImage(Message<String> message){
        client.insert("images", new JsonObject(message.body()), this::handler);
    }

    private void findFileByUUID(Message<String> message){
        final JsonObject query = new JsonObject()
                .put("uuid", message.body());
        client.find("images", query,
                result -> message.reply(Json.encode(result.result())));
    }

    private void handler(AsyncResult<String> stringAsyncResult) {
        if (stringAsyncResult.succeeded()) {
            logger.info("MongoDB save: " + stringAsyncResult.result());
        } else {
            logger.info("ERROR MongoDB: " + stringAsyncResult.cause());
        }
    }
}
