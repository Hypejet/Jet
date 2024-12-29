package net.hypejet.jet.server.registry.writers.registry.dimension;

import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.server.registry.writers.key.PackedKeyBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.key.TagKeyBinaryTagWriter;
import net.hypejet.jet.server.registry.writers.number.IntegerProviderBinaryTagWriter;
import net.hypejet.jet.server.util.codec.Writer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Writer a writer}, which writes {@linkplain DimensionType a dimension type}
 * into {@linkplain CompoundBinaryTag a compound binary tag}.
 *
 * @since 1.0
 * @see DimensionType
 * @see CompoundBinaryTag
 * @see Writer
 */
public final class DimensionTypeBinaryTagWriter implements Writer<DimensionType, CompoundBinaryTag> {
    /**
     * An instance of the {@linkplain DimensionTypeBinaryTagWriter dimension type binary tag writer}.
     *
     * @since 1.0
     */
    public static final DimensionTypeBinaryTagWriter INSTANCE = new DimensionTypeBinaryTagWriter();

    private static final String FIXED_TIME = "fixed_time";
    private static final String HAS_SKYLIGHT = "has_skylight";
    private static final String HAS_CEILING = "has_ceiling";
    private static final String ULTRAWARM = "ultrawarm";
    private static final String NATURAL = "natural";
    private static final String COORDINATE_SCALE = "coordinate_scale";
    private static final String BED_WORKS = "bed_works";
    private static final String RESPAWN_ANCHOR_WORKS = "respawn_anchor_works";
    private static final String MIN_Y = "min_y";
    private static final String HEIGHT = "height";
    private static final String LOGICAL_HEIGHT = "logical_height";
    private static final String INFINIBURN = "infiniburn";
    private static final String EFFECTS = "effects";
    private static final String AMBIENT_LIGHT = "ambient_light";
    private static final String PIGLIN_SAFE = "piglin_safe";
    private static final String HAS_RAIDS = "has_raids";
    private static final String MONSTER_SPAWN_LIGHT_LEVEL = "monster_spawn_light_level";
    private static final String MONSTER_SPAWN_BLOCK_LIGHT_LIMIT = "monster_spawn_block_light_limit";

    private DimensionTypeBinaryTagWriter() {}

    @Override
    public @NonNull CompoundBinaryTag write(@NonNull DimensionType object) {
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();

        Long fixedTime = object.fixedTime();
        if (fixedTime != null)
            builder.putLong(FIXED_TIME, fixedTime);

        return builder.putBoolean(HAS_SKYLIGHT, object.hasSkylight())
                .putBoolean(HAS_CEILING, object.hasCeiling())
                .putBoolean(ULTRAWARM, object.ultrawarm())
                .putBoolean(NATURAL, object.natural())
                .putDouble(COORDINATE_SCALE, object.coordinateScale())
                .putBoolean(BED_WORKS, object.bedWorks())
                .putBoolean(RESPAWN_ANCHOR_WORKS, object.respawnAnchorWorks())
                .putInt(MIN_Y, object.minY())
                .putInt(HEIGHT, object.height())
                .putInt(LOGICAL_HEIGHT, object.localHeight())
                .put(INFINIBURN, TagKeyBinaryTagWriter.INSTANCE.write(object.infiniburn()))
                .put(EFFECTS, PackedKeyBinaryTagWriter.INSTANCE.write(object.effects()))
                .putFloat(AMBIENT_LIGHT, object.ambientLight())
                .putBoolean(PIGLIN_SAFE, object.piglinSafe())
                .putBoolean(HAS_RAIDS, object.hasRaids())
                .put(MONSTER_SPAWN_LIGHT_LEVEL,
                        IntegerProviderBinaryTagWriter.INSTANCE.write(object.monsterSpawnLightLevel()))
                .putInt(MONSTER_SPAWN_BLOCK_LIGHT_LIMIT, object.monsterSpawnBlockLightLimit())
                .build();
    }
}