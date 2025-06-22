package practicum.http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter out, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            out.nullValue();
        } else {
            out.value(localDateTime.toString()); // Сериализуем в строку (например, "PT5H30M")
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        String localDateTimeStr = in.nextString();
        return localDateTimeStr != null ? LocalDateTime.parse(localDateTimeStr) : null;
    }
}