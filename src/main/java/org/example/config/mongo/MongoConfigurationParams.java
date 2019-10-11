package org.example.config.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "mongo")
@Component
public class MongoConfigurationParams {
    private String uri;
    private String dbName;
}
