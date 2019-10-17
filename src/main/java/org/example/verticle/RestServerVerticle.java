package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.config.handler.HttpHandlerConfiguration;
import org.example.config.handler.HttpRouterType;
import org.springframework.context.ApplicationContext;

public class RestServerVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(RestServerVerticle.class);

    private final int port;

    private final ApplicationContext applicationContext;

    public RestServerVerticle(int port, ApplicationContext applicationContext) {
        this.port = port;
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();
        Router httpRouter = Router.router(vertx);
        httpRouter.route().handler(BodyHandler.create());
        fillHttpHandlers(httpRouter);
        httpServer.requestHandler(httpRouter::accept);
        httpServer.listen(this.port);
        logger.info("Rest HttpServer start on port: " + port);
    }

    private void fillHttpHandlers(Router httpRouter) {
        HttpHandlerConfiguration.httpHandlerParams(applicationContext).forEach(httpHandlerParams -> {
            if (httpHandlerParams.getType().equals(HttpRouterType.GET)){
                httpRouter.get(httpHandlerParams.getUri())
                        .handler(httpHandlerParams.getHandler());
                logger.info("Add GET uri: "+httpHandlerParams.getUri());
            } else if (httpHandlerParams.getType().equals(HttpRouterType.POST)){
                httpRouter.post(httpHandlerParams.getUri())
                        .handler(httpHandlerParams.getHandler());
                logger.info("Add POST uri: "+httpHandlerParams.getUri());
            } else if (httpHandlerParams.getType().equals(HttpRouterType.PUT)) {
                httpRouter.put(httpHandlerParams.getUri())
                        .handler(httpHandlerParams.getHandler());
                logger.info("Add PUT uri: "+httpHandlerParams.getUri());
            } else if (httpHandlerParams.getType().equals(HttpRouterType.DELETE)){
                httpRouter.delete(httpHandlerParams.getUri())
                        .handler(httpHandlerParams.getHandler());
                logger.info("Add DELETE uri: "+httpHandlerParams.getUri());
            } else if (httpHandlerParams.getType().equals(HttpRouterType.FILE)) {
                httpRouter.post(httpHandlerParams.getUri())
                        .handler(BodyHandler.create()
                                .setMergeFormAttributes(true));
                httpRouter.post(httpHandlerParams.getUri())
                        .handler(httpHandlerParams.getHandler());
                logger.info("Add FILE uri: "+httpHandlerParams.getUri());
            } else {
                httpRouter.get(httpHandlerParams.getUri()).handler(httpHandlerParams.getHandler());
            }
        });
    }
}
