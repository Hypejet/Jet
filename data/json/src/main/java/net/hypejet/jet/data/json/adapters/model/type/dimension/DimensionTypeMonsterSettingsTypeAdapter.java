package net.hypejet.jet.data.json.adapters.model.type.dimension;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.JsonIntProvider;
import net.hypejet.jet.data.json.model.type.dimension.JsonDimensionType;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter}
 * of {@linkplain JsonDimensionType.MonsterSettings dimension type monster settings}.
 *
 * @since 1.0
 * @see JsonDimensionType.MonsterSettings
 * @see TypeAdapter
 */
final class DimensionTypeMonsterSettingsTypeAdapter extends TypeAdapter<JsonDimensionType.MonsterSettings> {

    private static final String PIGLIN_SAFE_FIELD = "piglin-safe";
    private static final String HAS_RAIDS_FIELD = "has-raids";
    private static final String SPAWN_LIGHT_TEST_FIELD = "spawn-light-test";
    private static final String SPAWN_BLOCK_LIGHT_LIMIT_FIELD = "spawn-block-light-limit";

    private final Gson gson;

    /**
     * Constructs the {@linkplain DimensionTypeMonsterSettingsTypeAdapter dimension type monster
     * settings type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    DimensionTypeMonsterSettingsTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonDimensionType.MonsterSettings value) throws IOException {
        out.beginObject();

        if (value.piglinSafe()) {
            out.name(PIGLIN_SAFE_FIELD);
            out.value(true);
        }

        if (value.hasRaids()) {
            out.name(HAS_RAIDS_FIELD);
            out.value(true);
        }

        out.name(SPAWN_LIGHT_TEST_FIELD);
        this.gson.toJson(value.spawnLightTest(), JsonIntProvider.class, out);

        out.name(SPAWN_BLOCK_LIGHT_LIMIT_FIELD);
        out.value(value.spawnBlockLightLimit());

        out.endObject();
    }

    @Override
    public JsonDimensionType.MonsterSettings read(JsonReader in) throws IOException {
        in.beginObject();

        boolean piglinSafe = false;
        boolean hasRaids = false;
        JsonIntProvider spawnLightTest = null;
        int spawnBlockLightLimit = 0;

        boolean spawnBlockLightLimitInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case PIGLIN_SAFE_FIELD -> piglinSafe = in.nextBoolean();
                case HAS_RAIDS_FIELD -> hasRaids = in.nextBoolean();
                case SPAWN_LIGHT_TEST_FIELD -> spawnLightTest = this.gson.fromJson(in, JsonIntProvider.class);
                case SPAWN_BLOCK_LIGHT_LIMIT_FIELD -> {
                    spawnBlockLightLimit = in.nextInt();
                    spawnBlockLightLimitInitialized = true;
                }
            }
        }

        in.endObject();

        if (!spawnBlockLightLimitInitialized)
            throw new JsonParseException("The spawn block light limit has not been specified");
        return new JsonDimensionType.MonsterSettings(piglinSafe, hasRaids, spawnLightTest, spawnBlockLightLimit);
    }
}