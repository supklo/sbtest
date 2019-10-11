package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
//        deploy(Vertx.vertx());
    }

//    private static void deploy(Vertx vertx) {
//        vertx.deployVerticle(new WsServerVerticle());
//        vertx.deployVerticle(new RestServerVerticle());
//        vertx.deployVerticle(new ClientServerVerticle());
//        vertx.deployVerticle(new RouterVerticle());
//        vertx.deployVerticle(new MongoDbVerticle());
//    }
}
