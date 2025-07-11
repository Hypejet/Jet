package net.hypejet.jet.data.json.adapters.model.variant.wolf;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfVariant;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonWolfVariant wolf variants}.
 *
 * @since 1.0
 * @see JsonWolfVariant
 * @see TypeAdapter
 */
final class WolfVariantTypeAdapter extends TypeAdapter<JsonWolfVariant> {

    private static final String WILD_ASSET_FIELD = "wild";
    private static final String TAME_ASSET_FIELD = "tame";
    private static final String ANGRY_ASSET_FIELD = "angry";

    private final Gson gson;

    /**
     * Constructs the {@linkplain WolfVariantTypeAdapter wolf variant type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    WolfVariantTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonWolfVariant value) throws IOException {
        out.beginObject();

        out.name(WILD_ASSET_FIELD);
        this.gson.toJson(value.wildAsset(), Key.class, out);

        out.name(TAME_ASSET_FIELD);
        this.gson.toJson(value.tameAsset(), Key.class, out);

        out.name(ANGRY_ASSET_FIELD);
        this.gson.toJson(value.angryAsset(), Key.class, out);

        out.endObject();
    }

    @Override
    public JsonWolfVariant read(JsonReader in) throws IOException {
        in.beginObject();

        Key wildAsset = null;
        Key tameAsset = null;
        Key angryAsset = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case WILD_ASSET_FIELD -> wildAsset = this.gson.fromJson(in, Key.class);
                case TAME_ASSET_FIELD -> tameAsset = this.gson.fromJson(in, Key.class);
                case ANGRY_ASSET_FIELD -> angryAsset = this.gson.fromJson(in, Key.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (wildAsset == null) {
            throw new JsonParseException("The wild asset has not been specified");
        } else if (tameAsset == null) {
            throw new JsonParseException("The tame asset has not been specified");
        } else if (angryAsset == null) {
            throw new JsonParseException("The angry asset has not been specified");
        }

        return new JsonWolfVariant(wildAsset, tameAsset, angryAsset);
    }
}