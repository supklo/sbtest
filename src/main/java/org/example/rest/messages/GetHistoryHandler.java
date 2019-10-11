package org.example.rest.messages;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.example.config.handler.HttpRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@HttpRouter(uri = "/getHistory")
@Component
public class GetHistoryHandler implements Handler<RoutingContext> {
    private final Vertx vertx;

    @Autowired
    public GetHistoryHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(RoutingContext context) {
        vertx.eventBus().send("getHistory", context.getBodyAsString(), result ->
                context.response().end(result.result().body().toString())
        );
    }
}
