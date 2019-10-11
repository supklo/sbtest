package org.example.data;

import io.vertx.core.json.Json;
import lombok.Data;

@Data
public class MessageData {
    private String address;
    private String text;

    @Override
    public String toString() {
        return Json.encodePrettily(this);
    }
}
