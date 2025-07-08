package net.hypejet.jet.data.json.adapters.model;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonWeighted;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonWeighted weighted}.
 *
 * @param <V> a value type of weighted that this type adapter converts
 * @since 1.0
 * @see JsonWeighted
 * @see TypeAdapter
 */
public final class WeightedTypeAdapter<V> extends TypeAdapter<JsonWeighted<V>> {

    private static final String VALUE_FIELD = "value";
    private static final String WEIGHT_FIELD = "weight";

    private final Gson gson;
    private final Class<V> valueClass;

    /**
     * Constructs the {@linkplain WeightedTypeAdapter weighted type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @param valueClass a value class of weighted that the weighted type adapter should convert
     * @since 1.0
     */
    public WeightedTypeAdapter(@NonNull Gson gson, @NonNull Class<V> valueClass) {
        this.gson = Objects.requireNonNull(gson, "gson");
        this.valueClass = Objects.requireNonNull(valueClass, "value class");
    }

    @Override
    public void write(JsonWriter out, JsonWeighted<V> value) throws IOException {
        out.beginObject();

        out.name(VALUE_FIELD);
        this.gson.toJson(value.value(), this.valueClass, out);

        out.name(WEIGHT_FIELD);
        out.value(value.weight());

        out.endObject();
    }

    @Override
    public JsonWeighted<V> read(JsonReader in) throws IOException {
        in.beginObject();

        V value = null;
        int weight = 0;
        boolean weightInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case VALUE_FIELD -> value = this.gson.fromJson(in, this.valueClass);
                case WEIGHT_FIELD -> {
                    weight = in.nextInt();
                    weightInitialized = true;
                }
                default -> throw new JsonParseException("Unknown field: " + value);
            }
        }

        if (value == null) {
            throw new JsonParseException("The value field has not been specified");
        } else if (!weightInitialized) {
            throw new JsonParseException("The weight field has not been specified");
        }

        return new JsonWeighted<>(value, weight);
    }
}