package net.hypejet.jet.data.json.adapters.model.variant.wolf;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.variant.wolf.JsonWolfSoundVariant;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonWolfSoundVariant wolf sound variants}.
 *
 * @since 1.0
 * @see JsonWolfSoundVariant
 * @see TypeAdapter
 */
public final class WolfSoundVariantTypeAdapter extends TypeAdapter<JsonWolfSoundVariant> {

    private static final String AMBIENT_SOUND_FIELD = "ambient";
    private static final String DEATH_SOUND_FIELD = "death";
    private static final String GROWL_SOUND_FIELD = "growl";
    private static final String HURT_SOUND_FIELD = "hurt";
    private static final String PANT_SOUND_FIELD = "pant";
    private static final String WHINE_SOUND_FIELD = "whine";

    private final Gson gson;

    /**
     * Constructs the {@linkplain WolfSoundVariantTypeAdapter wolf sound variant type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    public WolfSoundVariantTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonWolfSoundVariant value) throws IOException {
        out.beginObject();

        out.name(AMBIENT_SOUND_FIELD);
        this.gson.toJson(value.ambientSound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(DEATH_SOUND_FIELD);
        this.gson.toJson(value.deathSound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(GROWL_SOUND_FIELD);
        this.gson.toJson(value.growlSound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(HURT_SOUND_FIELD);
        this.gson.toJson(value.hurtSound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(PANT_SOUND_FIELD);
        this.gson.toJson(value.pantSound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(WHINE_SOUND_FIELD);
        this.gson.toJson(value.whineSound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.endObject();
    }

    @Override
    public JsonWolfSoundVariant read(JsonReader in) throws IOException {
        in.beginObject();

        JsonHolder<JsonSoundEvent> ambientSound = null;
        JsonHolder<JsonSoundEvent> deathSound = null;
        JsonHolder<JsonSoundEvent> growlSound = null;
        JsonHolder<JsonSoundEvent> hurtSound = null;
        JsonHolder<JsonSoundEvent> pantSound = null;
        JsonHolder<JsonSoundEvent> whineSound = null;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case AMBIENT_SOUND_FIELD -> ambientSound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case DEATH_SOUND_FIELD -> deathSound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case GROWL_SOUND_FIELD -> growlSound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case HURT_SOUND_FIELD -> hurtSound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case PANT_SOUND_FIELD -> pantSound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case WHINE_SOUND_FIELD -> whineSound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
            }
        }

        in.endObject();

        if (ambientSound == null) {
            throw new JsonParseException("The ambient sound has not been specified");
        } else if (deathSound == null) {
            throw new JsonParseException("The death sound has not been specified");
        } else if (growlSound == null) {
            throw new JsonParseException("The growl sound has not been specified");
        } else if (hurtSound == null) {
            throw new JsonParseException("The hurt sound has not been specified");
        } else if (pantSound == null) {
            throw new JsonParseException("The pant sound has not been specified");
        } else if (whineSound == null) {
            throw new JsonParseException("The whine sound has not been specified");
        }

        return new JsonWolfSoundVariant(ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound);
    }
}