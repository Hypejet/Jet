package net.hypejet.jet.data.json.adapters.model.pattern.banner;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.pattern.banner.JsonBannerPattern;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonBannerPattern banner patterns}.
 *
 * @since 1.0
 * @see JsonBannerPattern
 * @see TypeAdapter
 */
final class BannerPatternTypeAdapter extends TypeAdapter<JsonBannerPattern> {

    private static final String ASSET_FIELD = "asset";
    private static final String TRANSLATION_KEY_FIELD = "translation-key";

    private final Gson gson;

    /**
     * Constructs the {@linkplain BannerPatternTypeAdapter banner pattern type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    BannerPatternTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonBannerPattern value) throws IOException {
        out.beginObject();

        out.name(ASSET_FIELD);
        this.gson.toJson(value.asset(), Key.class, out);

        out.name(TRANSLATION_KEY_FIELD);
        out.value(value.translationKey());

        out.endObject();
    }

    @Override
    public JsonBannerPattern read(JsonReader in) throws IOException {
        in.beginObject();

        Key asset = null;
        String translationKey = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case ASSET_FIELD -> asset = this.gson.fromJson(in, Key.class);
                case TRANSLATION_KEY_FIELD -> translationKey = in.nextString();
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (asset == null) {
            throw new JsonParseException("The asset has not been specified");
        } else if (translationKey == null) {
            throw new JsonParseException("The translation key has not been specified");
        }

        return new JsonBannerPattern(asset, translationKey);
    }
}