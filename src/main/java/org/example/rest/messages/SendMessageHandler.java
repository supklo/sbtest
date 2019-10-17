package org.example.rest.messages;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.example.config.handler.HttpRouter;
import org.example.config.handler.HttpRouterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@HttpRouter(uri = "/sendMessage", type = HttpRouterType.POST)
@Component
public class SendMessageHandler implements Handler<RoutingContext> {
    private final Vertx vertx;

    @Autowired
    public SendMessageHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(RoutingContext context) {
        vertx.eventBus().send("router", context.getBodyAsString());
        context.response().end("ok");
    }
}
