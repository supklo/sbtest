package org.example.rest.images;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.Json;
import io.vertx.core.streams.Pump;
import io.vertx.ext.web.RoutingContext;
import org.example.config.handler.HttpRouter;
import org.example.config.handler.HttpRouterType;
import org.example.data.ImageData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@HttpRouter(uri = "/img/download/:id", type = HttpRouterType.GET)
@Component
public class DownloadFileHandler implements Handler<RoutingContext> {
    private final Vertx vertx;
    private final String storePath;
    private final TypeReference<List<ImageData>> typeReference = new TypeReference<List<ImageData>>() {
    };

    public DownloadFileHandler(Vertx vertx, @Value("${store.path}") String storePath) {
        this.vertx = vertx;
        this.storePath = storePath;
    }

    @Override
    public void handle(RoutingContext context) {
        final String uuid = context.request().getParam("id");
        vertx.eventBus().send("images.findImageByUUID", uuid, result -> {
                    List<ImageData> imageDataList = Json.decodeValue(result.result().body().toString(), typeReference);
                    if (!imageDataList.isEmpty()) {
                        sendFile(context, uuid);
                    } else {
                        context.response().setStatusCode(404).end();
                    }
                }
        );
    }

    private void sendFile(RoutingContext context, String uuid) {
        vertx.fileSystem().open(storePath+"/"+uuid, new OpenOptions(), readEvent -> {
            if (readEvent.failed()) {
                context.response().setStatusCode(500).end();
            } else {
                final AsyncFile asyncFile = readEvent.result();
                context.response().setChunked(true);
                Pump pump = Pump.pump(asyncFile, context.response());
                pump.start();
                asyncFile.endHandler(aVoid -> {
                    asyncFile.close();
                    context.response().end();
                });
            }
        });
    }
}
