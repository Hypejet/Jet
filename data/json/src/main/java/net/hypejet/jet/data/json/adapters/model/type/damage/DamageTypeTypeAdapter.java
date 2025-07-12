package net.hypejet.jet.data.json.adapters.model.type.damage;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageEffects;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageScalingType;
import net.hypejet.jet.data.json.model.type.damage.JsonDamageType;
import net.hypejet.jet.data.json.model.type.damage.JsonDeathMessageType;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonDamageType damage types}.
 *
 * @since 1.0
 * @see JsonDamageEffects
 * @see TypeAdapter
 */
final class DamageTypeTypeAdapter extends TypeAdapter<JsonDamageType> {

    private static final String MESSAGE_ID_FIELD = "message";
    private static final String SCALING_TYPE_FIELD = "scaling";
    private static final String EXHAUSTION_FIELD = "exhaustion";
    private static final String EFFECTS_FIELD = "effects";
    private static final String MESSAGE_TYPE_FIELD = "message-type";

    private final Gson gson;

    /**
     * Constructs the {@linkplain DamageTypeTypeAdapter damage type type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    DamageTypeTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonDamageType value) throws IOException {
        out.beginObject();

        out.name(MESSAGE_ID_FIELD);
        out.value(value.messageId());

        out.name(SCALING_TYPE_FIELD);
        this.gson.toJson(value.scalingType(), JsonDamageScalingType.class, out);

        out.name(EXHAUSTION_FIELD);
        out.value(value.exhaustion());

        out.name(EFFECTS_FIELD);
        this.gson.toJson(value.effects(), JsonDamageEffects.class, out);

        out.name(MESSAGE_TYPE_FIELD);
        this.gson.toJson(value.messageType(), JsonDeathMessageType.class, out);

        out.endObject();
    }

    @Override
    public JsonDamageType read(JsonReader in) throws IOException {
        in.beginObject();

        String messageId = null;
        JsonDamageScalingType scalingType = null;
        float exhaustion = 0;
        JsonDamageEffects effects = null;
        JsonDeathMessageType messageType = null;

        boolean exhaustionInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case MESSAGE_ID_FIELD -> messageId = in.nextString();
                case SCALING_TYPE_FIELD -> scalingType = this.gson.fromJson(in, JsonDamageScalingType.class);
                case EXHAUSTION_FIELD -> {
                    exhaustion = (float) in.nextDouble();
                    exhaustionInitialized = true;
                }
                case EFFECTS_FIELD -> effects = this.gson.fromJson(in, JsonDamageEffects.class);
                case MESSAGE_TYPE_FIELD -> messageType = this.gson.fromJson(in, JsonDeathMessageType.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (messageId == null) {
            throw new JsonParseException("The message id has not been specified");
        } else if (scalingType == null) {
            throw new JsonParseException("The scaling type has not been specified");
        } else if (!exhaustionInitialized) {
            throw new JsonParseException("The exhaustion has not been specified");
        } else if (effects == null) {
            throw new JsonParseException("The effects have not been specified");
        } else if (messageType == null) {
            throw new JsonParseException("The message type has not been specified");
        }

        return new JsonDamageType(messageId, scalingType, exhaustion, effects, messageType);
    }
}