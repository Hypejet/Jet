package net.hypejet.jet.data.json.adapters.model.type.dimension;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.type.dimension.JsonDimensionType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonDimensionType dimension types}.
 *
 * @since 1.0
 * @see JsonDimensionType
 * @see TypeAdapter
 */
final class DimensionTypeTypeAdapter extends TypeAdapter<JsonDimensionType> {

    private static final String FIXED_TIME_FIELD = "fixed-time";
    private static final String HAS_SKY_LIGHT_FIELD = "has-sky-light";
    private static final String HAS_CEILING_FIELD = "has-ceiling";
    private static final String ULTRA_WARM_FIELD = "ultra-warm";
    private static final String NATURAL_FIELD = "natural";
    private static final String COORDINATE_SCALE_FIELD = "coordinate-scale";
    private static final String BED_WORKS_FIELD = "bed-works";
    private static final String RESPAWN_ANCHOR_WORKS_FIELD = "respawn-anchor-works";
    private static final String MIN_Y_FIELD = "min-y";
    private static final String HEIGHT_FIELD = "height";
    private static final String LOCAL_HEIGHT_FIELD = "local-height";
    private static final String INFINIBURN_FIELD = "infiniburn";
    private static final String EFFECTS_FIELD = "effects";
    private static final String AMBIENT_LIGHT_FIELD = "ambient-light";
    private static final String CLOUD_HEIGHT_FIELD = "cloud-height";
    private static final String MONSTER_SETTINGS_FIELD = "monster-settings";

    private final Gson gson;

    /**
     * Constructs the {@linkplain DimensionTypeTypeAdapter dimension type type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    DimensionTypeTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonDimensionType value) throws IOException {
        out.beginObject();

        Long fixedTime = value.fixedTime();
        if (fixedTime != null) {
            out.name(FIXED_TIME_FIELD);
            out.value(fixedTime);
        }

        if (value.hasSkyLight()) {
            out.name(HAS_SKY_LIGHT_FIELD);
            out.value(true);
        }

        if (value.hasCeiling()) {
            out.name(HAS_CEILING_FIELD);
            out.value(true);
        }

        if (value.ultraWarm()) {
            out.name(ULTRA_WARM_FIELD);
            out.value(true);
        }

        if (value.natural()) {
            out.name(NATURAL_FIELD);
            out.value(true);
        }

        out.name(COORDINATE_SCALE_FIELD);
        out.value(value.coordinateScale());

        if (value.bedWorks()) {
            out.name(BED_WORKS_FIELD);
            out.value(true);
        }

        if (value.respawnAnchorWorks()) {
            out.name(RESPAWN_ANCHOR_WORKS_FIELD);
            out.value(true);
        }

        out.name(MIN_Y_FIELD);
        out.value(value.minY());

        out.name(HEIGHT_FIELD);
        out.value(value.height());

        out.name(LOCAL_HEIGHT_FIELD);
        out.value(value.localHeight());

        out.name(INFINIBURN_FIELD);
        this.gson.toJson(value.infiniburn(), Key.class, out);

        out.name(EFFECTS_FIELD);
        this.gson.toJson(value.effects(), Key.class, out);

        out.name(AMBIENT_LIGHT_FIELD);
        out.value(value.ambientLight());

        out.name(CLOUD_HEIGHT_FIELD);
        out.value(value.cloudHeight());

        out.name(MONSTER_SETTINGS_FIELD);
        this.gson.toJson(value.monsterSettings(), JsonDimensionType.MonsterSettings.class, out);

        out.endObject();
    }

    @Override
    public JsonDimensionType read(JsonReader in) throws IOException {
        in.beginObject();

        Long fixedTime = null;
        boolean hasSkyLight = false;
        boolean hasCeiling = false;
        boolean ultraWarm = false;
        boolean natural = false;
        double coordinateScale = 0;
        boolean bedWorks = false;
        boolean respawnAnchorWorks = false;
        int minY = 0;
        int height = 0;
        int localHeight = 0;
        Key infiniburn = null;
        Key effects = null;
        float ambientLight = 0;
        int cloudHeight = 0;
        JsonDimensionType.MonsterSettings monsterSettings = null;

        boolean coordinateScaleInitialized = false;
        boolean minYInitialized = false;
        boolean heightInitialized = false;
        boolean localHeightInitialized = false;
        boolean ambientLightInitialized = false;
        boolean cloudHeightInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case FIXED_TIME_FIELD -> fixedTime = in.nextLong();
                case HAS_SKY_LIGHT_FIELD -> hasSkyLight = in.nextBoolean();
                case HAS_CEILING_FIELD -> hasCeiling = in.nextBoolean();
                case ULTRA_WARM_FIELD -> ultraWarm = in.nextBoolean();
                case COORDINATE_SCALE_FIELD -> {
                    coordinateScale = in.nextDouble();
                    coordinateScaleInitialized = true;
                }
                case BED_WORKS_FIELD -> bedWorks = in.nextBoolean();
                case RESPAWN_ANCHOR_WORKS_FIELD -> respawnAnchorWorks = in.nextBoolean();
                case MIN_Y_FIELD -> {
                    minY = in.nextInt();
                    minYInitialized = true;
                }
                case HEIGHT_FIELD -> {
                    height = in.nextInt();
                    heightInitialized = true;
                }
                case LOCAL_HEIGHT_FIELD -> {
                    localHeight = in.nextInt();
                    localHeightInitialized = true;
                }
                case INFINIBURN_FIELD -> infiniburn = this.gson.fromJson(in, Key.class);
                case EFFECTS_FIELD -> effects = this.gson.fromJson(in, Key.class);
                case AMBIENT_LIGHT_FIELD -> {
                    ambientLight = (float) in.nextDouble();
                    ambientLightInitialized = true;
                }
                case CLOUD_HEIGHT_FIELD -> {
                    cloudHeight = in.nextInt();
                    cloudHeightInitialized = true;
                }
                case MONSTER_SETTINGS_FIELD ->
                        monsterSettings = this.gson.fromJson(in, JsonDimensionType.MonsterSettings.class);
                default -> throw new JsonParseException("Unknown field: " + name);
            }
        }

        in.endObject();

        if (!coordinateScaleInitialized) {
            throw new JsonParseException("The coordinate scale has not been specified");
        } else if (!minYInitialized) {
            throw new JsonParseException("The min Y has not been specified");
        } else if (!heightInitialized) {
            throw new JsonParseException("The height has not been specified");
        } else if (!localHeightInitialized) {
            throw new JsonParseException("The local height has not been specified");
        } else if (infiniburn == null) {
            throw new JsonParseException("The infiniburn has not been specified");
        } else if (effects == null) {
            throw new JsonParseException("The effects have not been specified");
        } else if (!ambientLightInitialized) {
            throw new JsonParseException("The ambient light has not been specified");
        } else if (!cloudHeightInitialized) {
            throw new JsonParseException("The cloud height has not been specified");
        } else if (monsterSettings == null) {
            throw new JsonParseException("The monster settings have not been specified");
        }

        return new JsonDimensionType(
                fixedTime, hasSkyLight, hasCeiling, ultraWarm, natural, coordinateScale, bedWorks, respawnAnchorWorks,
                minY, height, localHeight, infiniburn, effects, ambientLight, cloudHeight, monsterSettings
        );
    }
}