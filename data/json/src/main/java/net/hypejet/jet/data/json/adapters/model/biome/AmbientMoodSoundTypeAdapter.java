package net.hypejet.jet.data.json.adapters.model.biome;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonHolder;
import net.hypejet.jet.data.json.model.JsonSoundEvent;
import net.hypejet.jet.data.json.model.biome.JsonAmbientMoodSound;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonAmbientMoodSound ambient mood sounds}.
 *
 * @since 1.0
 * @see JsonAmbientMoodSound
 * @see TypeAdapter
 */
final class AmbientMoodSoundTypeAdapter extends TypeAdapter<JsonAmbientMoodSound> {

    private static final String SOUND_FIELD = "sound";
    private static final String TICK_DELAY_FIELD = "tick-delay";
    private static final String BLOCK_SEARCH_EXTENT_FIELD = "block-search-extent";
    private static final String OFFSET_FIELD = "offset";

    private final Gson gson;

    /**
     * Constructs the {@linkplain AmbientMoodSoundTypeAdapter ambient mood sound type adpater}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    AmbientMoodSoundTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonAmbientMoodSound value) throws IOException {
        out.beginObject();

        out.name(SOUND_FIELD);
        this.gson.toJson(value.sound(), DataJsonTypes.SOUND_EVENT_HOLDER, out);

        out.name(TICK_DELAY_FIELD);
        out.value(value.tickDelay());

        out.name(BLOCK_SEARCH_EXTENT_FIELD);
        out.value(value.blockSearchExtent());

        out.name(OFFSET_FIELD);
        out.value(value.offset());

        out.endObject();
    }

    @Override
    public JsonAmbientMoodSound read(JsonReader in) throws IOException {
        in.beginObject();

        JsonHolder<JsonSoundEvent> sound = null;
        int tickDelay = 0;
        int blockSearchExtent = 0;
        double offset = 0;

        boolean tickDelayInitialized = false;
        boolean blockSearchExtentInitialized = false;
        boolean offsetInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case SOUND_FIELD -> sound = this.gson.fromJson(in, DataJsonTypes.SOUND_EVENT_HOLDER);
                case TICK_DELAY_FIELD -> {
                    tickDelay = in.nextInt();
                    tickDelayInitialized = true;
                }
                case BLOCK_SEARCH_EXTENT_FIELD -> {
                    blockSearchExtent = in.nextInt();
                    blockSearchExtentInitialized = true;
                }
                case OFFSET_FIELD -> {
                    offset = in.nextDouble();
                    offsetInitialized = true;
                }
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (sound == null) {
            throw new JsonParseException("The sound field has not been specified");
        } else if (!tickDelayInitialized) {
            throw new JsonParseException("The tick delay field has not been specified");
        } else if (!blockSearchExtentInitialized) {
            throw new JsonParseException("The block search extent field has not been specified");
        } else if (!offsetInitialized) {
            throw new JsonParseException("The offset field has not been initialized");
        }

        return new JsonAmbientMoodSound(sound, tickDelay, blockSearchExtent, offset);
    }
}