package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class ClientServerVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(ClientServerVerticle.class);

    private final int port;

    public ClientServerVerticle(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();
        Router httpRouter = getRouter();
        httpServer.requestHandler(httpRouter::accept);
        httpServer.listen(port);
        logger.info("HttpServer started on port: "+port);
    }

    private Router getRouter() {
        Router httpRouter = Router.router(vertx);
        httpRouter.route("/*")
                .handler(StaticHandler.create()
                        .setCachingEnabled(false)
                        .setWebRoot("static")
                );
        return httpRouter;
    }
}
