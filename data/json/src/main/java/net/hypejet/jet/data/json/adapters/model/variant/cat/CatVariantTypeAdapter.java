package net.hypejet.jet.data.json.adapters.model.variant.cat;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.cat.JsonCatVariant;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonCatVariant cat variants}.
 *
 * @since 1.0
 * @see JsonCatVariant
 * @see TypeAdapter
 */
final class CatVariantTypeAdapter extends TypeAdapter<JsonCatVariant> {

    private final Gson gson;

    /**
     * Constructs the {@linkplain CatVariantTypeAdapter cat variant type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    CatVariantTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonCatVariant value) {
        this.gson.toJson(value.asset(), Key.class, out);
    }

    @Override
    public JsonCatVariant read(JsonReader in) {
        return new JsonCatVariant(this.gson.fromJson(in, Key.class));
    }
}