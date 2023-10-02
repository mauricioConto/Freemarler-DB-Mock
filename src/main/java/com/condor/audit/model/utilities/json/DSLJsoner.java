package com.condor.audit.model.utilities.json;

import com.condor.audit.model.utilities.JsonBase;
import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonConverter;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.runtime.Settings;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DSLJsoner {
    private final DslJson<Object> dslJson;

    public DSLJsoner() {
        dslJson = new DslJson<>(Settings.withRuntime().allowArrayFormat(true).includeServiceLoader());
    }

    public
    JsonBase deserializeJsonPayload(String json) throws IOException {
        return deserialize(String.format("{\"event\":%s}", json));
    }

    public JsonBase deserialize(String json) throws IOException {
        try (InputStream ip = new ByteArrayInputStream(json.getBytes())) {
            return dslJson.deserialize(JsonBase.class, ip);
        }
    }

    public <T> T deserialize(String json, Class<T> clazz) throws IOException {
        try (InputStream ip = new ByteArrayInputStream(json.getBytes())) {
            return dslJson.deserialize(clazz, ip);
        }
    }

    public <T> List<T> deserializeToList(String customerInfo, Class<T> clazz) {
        try {
            if (Objects.isNull(customerInfo)) {
                return new ArrayList<>();
            }
            InputStream ip = new ByteArrayInputStream(customerInfo.getBytes(StandardCharsets.UTF_8));
            return dslJson.deserializeList(clazz, ip);
        } catch (final IOException e) {
            return new ArrayList<>();
        }
    }

    public <T> String serialize(T input) {
        try {
            JsonWriter jsonWriter = dslJson.newWriter();
            dslJson.serialize(jsonWriter, input);
            return jsonWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @JsonConverter(target = LocalDateTime.class)
    public static abstract class LocalDateTimeConvertor {
        public static final JsonReader.ReadObject<LocalTime> JSON_READER = reader -> {
            if (reader.wasNull()) return null;
            return LocalTime.parse(reader.readSimpleString());
        };
        public static final JsonWriter.WriteObject<LocalTime> JSON_WRITER = (writer, value) -> {
            if (value == null) {
                writer.writeNull();
            } else {
                writer.writeString(value.toString());
            }
        };
    }


    @JsonConverter(target = BigDecimal.class)
    public static abstract class BigDecimalConvertor {
        public static final JsonReader.ReadObject<LocalTime> JSON_READER = reader -> {
            if (reader.wasNull()) return null;
            return LocalTime.parse(reader.readSimpleString());
        };
        public static final JsonWriter.WriteObject<LocalTime> JSON_WRITER = (writer, value) -> {
            if (value == null) {
                writer.writeNull();
            } else {
                writer.writeString(value.toString());
            }
        };
    }
}
