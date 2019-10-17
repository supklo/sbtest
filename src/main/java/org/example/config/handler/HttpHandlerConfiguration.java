package org.example.config.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.stream.Collectors;

public class HttpHandlerConfiguration {

    private HttpHandlerConfiguration(){}

    public static Collection<HttpHandlerParams> httpHandlerParams(ApplicationContext applicationContext) {
        return applicationContext.getBeansWithAnnotation(HttpRouter.class)
                .entrySet().stream().map(entry -> {
                    HttpRouter[] declaredAnnotationsByType = entry.getValue().getClass().getDeclaredAnnotationsByType(HttpRouter.class);
                    HttpHandlerParams httpHandlerParams = HttpHandlerParams.builder()
                            .type(declaredAnnotationsByType[0].type())
                            .uri(declaredAnnotationsByType[0].uri())
                            .handler((Handler<RoutingContext>) entry.getValue())
                            .build();
                    return httpHandlerParams;
                }).collect(Collectors.toList());
    }
}
