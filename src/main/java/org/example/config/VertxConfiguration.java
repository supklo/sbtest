package org.example.config;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.example.config.mongo.MongoConfigurationParams;
import org.example.verticle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertxConfiguration {
    private final ApplicationContext applicationContext;
    private final int restPort;
    private final int wsPort;
    private final int staticPort;
    private final MongoConfigurationParams mongoConfigurationParams;

    @Autowired
    public VertxConfiguration(
            ApplicationContext applicationContext,
            MongoConfigurationParams mongoConfigurationParams,
            @Value("${server.rest.port}") int restPort,
            @Value("${server.ws.port}") int wsPort,
            @Value("${server.static.port}") int staticPort) {
        this.applicationContext = applicationContext;
        this.restPort = restPort;
        this.wsPort = wsPort;
        this.staticPort = staticPort;
        this.mongoConfigurationParams = mongoConfigurationParams;
    }

    @ConditionalOnMissingBean
    @Bean
    public Vertx vertx() {
        return Vertx.vertx();
    }

    @Bean
    public RestServerVerticle restServerVerticle() {
        RestServerVerticle restServerVerticle = new RestServerVerticle(restPort, applicationContext);
        vertx().deployVerticle(restServerVerticle);
        return restServerVerticle;
    }

    @Bean
    public ClientServerVerticle clientServerVerticle() {
        ClientServerVerticle clientServerVerticle = new ClientServerVerticle(staticPort);
        vertx().deployVerticle(clientServerVerticle);
        return clientServerVerticle;
    }

    @Bean
    public WsServerVerticle wsServerVerticle(){
        WsServerVerticle wsServerVerticle = new WsServerVerticle(wsPort);
        vertx().deployVerticle(wsServerVerticle);
        return wsServerVerticle;
    }

    @Bean
    public RouterVerticle routerVerticle(){
        RouterVerticle routerVerticle = new RouterVerticle();
        vertx().deployVerticle(routerVerticle);
        return routerVerticle;
    }

    @Bean
    public MongoDbVerticle mongoDbVerticle(){
        MongoDbVerticle mongoDbVerticle = new MongoDbVerticle(mongoClient());
        vertx().deployVerticle(mongoDbVerticle);
        return mongoDbVerticle;
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClient.createShared(vertx(), new JsonObject()
                .put("db_name", mongoConfigurationParams.getDbName())
                .put("connection_string", mongoConfigurationParams.getUri())
        );
    }
}
