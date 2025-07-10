package net.hypejet.jet.data.json.adapters.model.trim.pattern;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.trim.pattern.JsonTrimPattern;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonTrimPattern trim patterns}.
 *
 * @since 1.0
 * @see JsonTrimPattern
 * @see TypeAdapter
 */
final class TrimPatternTypeAdapter extends TypeAdapter<JsonTrimPattern> {

    private static final String ASSET_FIELD = "asset";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String DECAL_FIELD = "decal";

    private final Gson gson;

    /**
     * Constructs the {@linkplain TrimPatternTypeAdapter trim pattern type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    TrimPatternTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonTrimPattern value) throws IOException {
        out.beginObject();

        out.name(ASSET_FIELD);
        this.gson.toJson(value.asset(), Key.class, out);

        out.name(DESCRIPTION_FIELD);
        this.gson.toJson(value.description(), Component.class, out);

        if (value.decal()) {
            out.name(DECAL_FIELD);
            out.value(true);
        }

        out.endObject();
    }

    @Override
    public JsonTrimPattern read(JsonReader in) throws IOException {
        in.beginObject();

        Key asset = null;
        Component description = null;
        boolean decal = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case ASSET_FIELD -> asset = this.gson.fromJson(in, Key.class);
                case DESCRIPTION_FIELD -> description = this.gson.fromJson(in, Component.class);
                case DECAL_FIELD -> in.nextBoolean();
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (asset == null) {
            throw new JsonParseException("The asset has not been specified");
        } else if (description == null) {
            throw new JsonParseException("The description has not been specified");
        }

        return new JsonTrimPattern(asset, description, decal);
    }
}