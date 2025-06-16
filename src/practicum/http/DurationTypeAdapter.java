package practicum.http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration duration) throws IOException {
        if (duration == null) {
            out.nullValue();
        } else {
            out.value(duration.toString()); // Сериализуем в строку (например, "PT5H30M")
        }
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        String durationStr = in.nextString();
        return durationStr != null ? Duration.parse(durationStr) : null;
    }
}