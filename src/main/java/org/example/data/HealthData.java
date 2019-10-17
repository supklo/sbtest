package org.example.data;

import io.vertx.core.json.Json;
import lombok.Data;

@Data
public class HealthData {
    private String status;

    @Override
    public String toString() {
        return Json.encodePrettily(this);
    }
}
