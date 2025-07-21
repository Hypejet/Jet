package net.hypejet.jet.data.json.adapters.model.block;

import com.google.common.primitives.ImmutableIntArray;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonBlock blocks}.
 *
 * @since 1.0
 * @see JsonBlock
 * @see TypeAdapter
 */
final class BlockTypeAdapter extends TypeAdapter<JsonBlock> {

    private static final String REQUIRED_FEATURE_FLAGS_FIELD = "required_feature_flags";
    private static final String DEFAULT_BLOCK_STATE_ID_FIELD = "default_state";
    private static final String BLOCK_STATE_IDS_FIELD = "states";

    private final Gson gson;

    /**
     * Constructs the {@linkplain BlockTypeAdapter block type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    BlockTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonBlock value) throws IOException {
        out.beginObject();

        Set<Key> requiredFeatureFlags = value.requiredFeatureFlags();
        if (!requiredFeatureFlags.isEmpty()) {
            out.name(REQUIRED_FEATURE_FLAGS_FIELD);
            this.gson.toJson(requiredFeatureFlags, DataJsonTypes.KEY_SET, out);
        }

        out.name(DEFAULT_BLOCK_STATE_ID_FIELD);
        out.value(value.defaultBlockStateId());

        out.name(BLOCK_STATE_IDS_FIELD);
        this.gson.toJson(value.blockStateIds(), ImmutableIntArray.class, out);

        out.endObject();
    }

    @Override
    public JsonBlock read(JsonReader in) throws IOException {
        in.beginObject();

        Set<Key> requiredFeatureFlags = Set.of();
        int defaultBlockStateId = 0;
        ImmutableIntArray blockStateIds = null;

        boolean defaultBlockStateIdInitialized = false;
        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case REQUIRED_FEATURE_FLAGS_FIELD ->
                        requiredFeatureFlags = this.gson.fromJson(in, DataJsonTypes.KEY_SET);
                case DEFAULT_BLOCK_STATE_ID_FIELD -> {
                    defaultBlockStateId = in.nextInt();
                    defaultBlockStateIdInitialized = true;
                }
                case BLOCK_STATE_IDS_FIELD -> blockStateIds = this.gson.fromJson(in, ImmutableIntArray.class);
            }
        }

        in.endObject();

        if (!defaultBlockStateIdInitialized) {
            throw new JsonParseException("The default block state id has not been specified");
        } else if (blockStateIds == null) {
            throw new JsonParseException("The block state ids have not been initialized");
        } else {
            return new JsonBlock(requiredFeatureFlags, defaultBlockStateId, blockStateIds);
        }
    }
}