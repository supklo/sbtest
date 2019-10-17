package org.example.config.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpHandlerParams {
    private final HttpRouterType type;
    private final String uri;
    private final Handler<RoutingContext> handler;
}
