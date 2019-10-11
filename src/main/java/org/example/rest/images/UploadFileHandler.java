package org.example.rest.images;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.example.config.handler.HttpRouter;
import org.example.config.handler.HttpRouterType;
import org.example.data.ImageData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@HttpRouter(uri = "/img/upload", type = HttpRouterType.FILE)
@Component
public class UploadFileHandler implements Handler<RoutingContext> {
    private final Logger logger = LoggerFactory.getLogger(UploadFileHandler.class);

    private final Vertx vertx;
    private final String storePath;

    public UploadFileHandler(Vertx vertx, @Value("${store.path}") String storePath) {
        this.vertx = vertx;
        this.storePath = storePath;
    }

    @PostConstruct
    private void init() {
        boolean mkdirs = new File(storePath).mkdirs();
        if (!mkdirs) {
            logger.info("Error create folder: " + storePath);
        }
    }

    @Override
    public void handle(RoutingContext context) {
        context.fileUploads().forEach(fileUpload -> saveAndResponse(context, fileUpload));
    }

    private void saveAndResponse(RoutingContext context, FileUpload fileUpload) {
        try {
            final String fileName = URLDecoder.decode(fileUpload.fileName(), "UTF-8");
            final String uuid = UUID.randomUUID().toString();

            final ImageData imageData = ImageData.builder()
                    .fileName(fileName)
                    .uuid(uuid)
                    .build();

            final String storeFileName = storePath + "/" + uuid;
            vertx.fileSystem()
                    .copy(
                            fileUpload.uploadedFileName(),
                            storeFileName,
                            handler -> logger.info("File " + uuid + " saved")
                    );
            vertx.eventBus().send("images.save", imageData.toString());
            context.response().end(imageData.toString());
        } catch (UnsupportedEncodingException e) {
            logger.error("Error save file", e);
            context.response().end("ERROR");
        }
    }
}
