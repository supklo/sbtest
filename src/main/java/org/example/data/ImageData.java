package org.example.data;

import io.vertx.core.json.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageData {
    private String _id;
    private String uuid;
    private String fileName;
    @Override
    public String toString() {
        return Json.encodePrettily(this);
    }
}
