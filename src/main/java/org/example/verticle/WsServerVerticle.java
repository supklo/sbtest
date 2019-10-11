package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class WsServerVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(WsServerVerticle.class);

    private final int port;

    public WsServerVerticle(int port) {
        this.port = port;
    }


    @Override
    public void start() {
        vertx.createHttpServer()
                .websocketHandler(this::createWebSocketServer)
                .listen(port);
        logger.info("WS Server start on port: " + port);
    }

    private void createWebSocketServer(ServerWebSocket wsServer) {
        logger.info("Create WebSocket: " + wsServer.path());
        wsServer.frameHandler(wsFrame -> {
            logger.info(wsFrame.textData());
            vertx.eventBus().send("router", wsFrame.textData());
        });

        MessageConsumer<String> consumerSendMessage = vertx.eventBus().consumer(wsServer.path(), data -> {
            wsServer.writeFinalTextFrame(data.body());
            data.reply("ok");
        });

        wsServer.closeHandler(aVoid -> {
            logger.info("Close WebSocket: " + consumerSendMessage.address());
            consumerSendMessage.unregister();
        });
    }
}
