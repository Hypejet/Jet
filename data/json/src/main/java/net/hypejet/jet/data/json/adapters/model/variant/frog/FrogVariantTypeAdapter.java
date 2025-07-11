package net.hypejet.jet.data.json.adapters.model.variant.frog;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.frog.JsonFrogVariant;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonFrogVariant frog variants}.
 *
 * @since 1.0
 * @see JsonFrogVariant
 * @see TypeAdapter
 */
final class FrogVariantTypeAdapter extends TypeAdapter<JsonFrogVariant> {

    private final Gson gson;

    /**
     * Constructs the {@linkplain FrogVariantTypeAdapter frog variant type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    FrogVariantTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonFrogVariant value) {
        this.gson.toJson(value.asset(), Key.class, out);
    }

    @Override
    public JsonFrogVariant read(JsonReader in) {
        return new JsonFrogVariant(this.gson.fromJson(in, Key.class));
    }
}