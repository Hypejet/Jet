package net.hypejet.jet.data.json.adapters.model.block;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.block.state.property.JsonEnumStatePropertyValueType;
import net.hypejet.jet.data.json.model.block.state.property.JsonStateProperty;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * A {@linkplain TypeAdapter type adapter} of {@linkplain JsonStateProperty state properties}.
 *
 * @since 1.0
 * @see JsonStateProperty
 * @see TypeAdapter
 */
@NullMarked
final class StatePropertyTypeAdapter extends TypeAdapter<JsonStateProperty> {

    private static final String TYPE_FIELD = "type";
    private static final String MIN_FIELD = "min";
    private static final String MAX_FIELD = "max";
    private static final String VALUE_TYPE_FIELD = "value_type";
    private static final String ACCEPTED_VALUES_FIELD = "accepted_values";

    private static final String TYPE_BOOLEAN = "boolean";
    private static final String TYPE_INTEGER = "integer";
    private static final String TYPE_ENUM = "enum";

    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 4;

    private final Gson gson;

    /**
     * Constructs the {@linkplain StatePropertyTypeAdapter state property type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    StatePropertyTypeAdapter(Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonStateProperty value) throws IOException {
        out.beginObject();

        out.name(TYPE_FIELD);
        switch (value) {
            case JsonStateProperty.Boolean ignored -> out.value(TYPE_BOOLEAN);
            case JsonStateProperty.Enum enumValue -> {
                out.value(TYPE_ENUM);
                out.name(VALUE_TYPE_FIELD);
                this.gson.toJson(enumValue.valueType(), JsonEnumStatePropertyValueType.class, out);
                out.name(ACCEPTED_VALUES_FIELD);
                this.gson.toJson(enumValue.acceptedValues(), DataJsonTypes.STRING_SET, out);
            }
            case JsonStateProperty.Integer integerValue -> {
                out.value(TYPE_INTEGER);

                int min = integerValue.min();
                if (min != DEFAULT_MIN) {
                    out.name(MIN_FIELD);
                    out.value(min);
                }

                int max = integerValue.max();
                if (max != DEFAULT_MAX) {
                    out.name(MAX_FIELD);
                    out.value(max);
                }
            }
        }

        out.endObject();
    }

    @Override
    public JsonStateProperty read(JsonReader in) throws IOException {
        in.beginObject();

        String type = null;
        int min = DEFAULT_MIN;
        int max = DEFAULT_MAX;
        JsonEnumStatePropertyValueType valueType = null;
        Set<String> acceptedValues = null;

        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case TYPE_FIELD -> type = in.nextString();
                case MIN_FIELD -> min = in.nextInt();
                case MAX_FIELD -> max = in.nextInt();
                case VALUE_TYPE_FIELD -> valueType = this.gson.fromJson(in, JsonEnumStatePropertyValueType.class);
                case ACCEPTED_VALUES_FIELD -> acceptedValues = this.gson.fromJson(in, DataJsonTypes.STRING_SET);
                default -> in.skipValue();
            }
        }

        in.endObject();
        if (type == null)
            throw new JsonParseException("The type has not been specified");

        return switch (type) {
            case TYPE_BOOLEAN -> JsonStateProperty.Boolean.INSTANCE;
            case TYPE_INTEGER -> new JsonStateProperty.Integer(min, max);
            case TYPE_ENUM -> {
                if (valueType == null) {
                    throw new JsonParseException("The value type has not been specified");
                } else if (acceptedValues == null) {
                    throw new JsonParseException("The accepted values have not been specified");
                } else {
                    yield new JsonStateProperty.Enum(valueType, acceptedValues);
                }
            }
            default -> throw new JsonParseException("Unknown state property type: " + type);
        };
    }
}