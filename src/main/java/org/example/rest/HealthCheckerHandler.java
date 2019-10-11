package org.example.rest;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.example.config.handler.HttpRouter;
import org.example.data.HealthData;
import org.springframework.stereotype.Component;

@HttpRouter(uri = "/health")
@Component
public class HealthCheckerHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext context) {
        context.response().end(new HealthData().toString());
    }
}
