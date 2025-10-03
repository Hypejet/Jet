package net.hypejet.jet.data.json.adapters.model.block;

import com.google.common.primitives.ImmutableIntArray;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.state.property.JsonStateProperty;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonBlock blocks}.
 *
 * @since 1.0
 * @see JsonBlock
 * @see TypeAdapter
 */
@NullMarked
final class BlockTypeAdapter extends TypeAdapter<JsonBlock> {

    private static final String REQUIRED_FEATURE_FLAGS_FIELD = "required_feature_flags";
    private static final String DEFAULT_BLOCK_STATE_ID_FIELD = "default_state";
    private static final String BLOCK_STATE_IDS_FIELD = "states";
    private static final String BLOCK_STATE_PROPERTIES_FIELD = "state_properties";

    private final Gson gson;

    /**
     * Constructs the {@linkplain BlockTypeAdapter block type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    BlockTypeAdapter(Gson gson) {
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

        Map<String, JsonStateProperty> stateProperties = value.blockStateProperties();
        if (!stateProperties.isEmpty()) {
            out.name(BLOCK_STATE_PROPERTIES_FIELD);
            this.gson.toJson(stateProperties, DataJsonTypes.STRING_TO_STATE_PROPERTY_MAP, out);
        }

        out.endObject();
    }

    @Override
    public JsonBlock read(JsonReader in) throws IOException {
        in.beginObject();

        Set<Key> requiredFeatureFlags = Set.of();
        int defaultBlockStateId = 0;
        ImmutableIntArray blockStateIds = null;
        Map<String, JsonStateProperty> stateProperties = Map.of();

        boolean defaultBlockStateIdInitialized = false;
        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case REQUIRED_FEATURE_FLAGS_FIELD ->
                        requiredFeatureFlags = this.gson.fromJson(in, DataJsonTypes.KEY_SET);
                case BLOCK_STATE_IDS_FIELD ->
                        blockStateIds = this.gson.fromJson(in, ImmutableIntArray.class);
                case BLOCK_STATE_PROPERTIES_FIELD ->
                        stateProperties = this.gson.fromJson(in, DataJsonTypes.STRING_TO_STATE_PROPERTY_MAP);
                case DEFAULT_BLOCK_STATE_ID_FIELD -> {
                    defaultBlockStateId = in.nextInt();
                    defaultBlockStateIdInitialized = true;
                }
            }
        }

        in.endObject();

        if (!defaultBlockStateIdInitialized) {
            throw new JsonParseException("The default block state id has not been specified");
        } else if (blockStateIds == null) {
            throw new JsonParseException("The block state ids have not been initialized");
        } else {
            return new JsonBlock(requiredFeatureFlags, defaultBlockStateId, blockStateIds, stateProperties);
        }
    }
}