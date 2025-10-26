package net.hypejet.jet.data.json.adapters.model.block;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.block.state.JsonBlockState;
import net.hypejet.jet.data.json.token.DataJsonTypes;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonBlockState block states}.
 *
 * @since 1.0
 * @see JsonBlockState
 * @see TypeAdapter
 */
final class BlockStateTypeAdapter extends TypeAdapter<JsonBlockState> {

    private static final String PROPERTIES_FIELD = "properties";
    private static final String IS_AIR_FIELD = "air";
    private static final String HAS_FLUID_STATE_FIELD = "has_fluid_state";
    private static final String BLOCKS_MOTION_FIELD = "blocks_motion";
    private static final String IS_LEAVES_FIELD = "leaves";

    private final Gson gson;

    /**
     * Constructs the {@linkplain BlockStateTypeAdapter block state type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    BlockStateTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonBlockState value) throws IOException {
        out.beginObject();

        Map<String, String> properties = value.properties();
        if (!properties.isEmpty()) {
            out.name(PROPERTIES_FIELD);
            this.gson.toJson(properties, DataJsonTypes.STRING_TO_STRING_MAP, out);
        }

        if (value.isAir()) {
            out.name(IS_AIR_FIELD);
            out.value(true);
        }

        if (value.hasFluidState()) {
            out.name(HAS_FLUID_STATE_FIELD);
            out.value(true);
        }

        if (!value.blocksMotion()) {
            out.name(BLOCKS_MOTION_FIELD);
            out.value(false);
        }

        if (value.isLeaves()) {
            out.name(IS_LEAVES_FIELD);
            out.value(true);
        }

        out.endObject();
    }

    @Override
    public JsonBlockState read(JsonReader in) throws IOException {
        in.beginObject();

        Map<String, String> properties = Map.of();
        boolean isAir = false;
        boolean hasFluidState = false;
        boolean blockMotion = true;
        boolean isLeaves = false;

        while (in.peek() == JsonToken.NAME) {
            switch (in.nextName()) {
                case PROPERTIES_FIELD -> properties = this.gson.fromJson(in, DataJsonTypes.STRING_TO_STRING_MAP);
                case IS_AIR_FIELD -> isAir = in.nextBoolean();
                case HAS_FLUID_STATE_FIELD -> hasFluidState = in.nextBoolean();
                case BLOCKS_MOTION_FIELD -> blockMotion = in.nextBoolean();
                case IS_LEAVES_FIELD -> isLeaves = in.nextBoolean();
            }
        }

        in.endObject();
        return new JsonBlockState(properties, isAir, hasFluidState, blockMotion, isLeaves);
    }
}