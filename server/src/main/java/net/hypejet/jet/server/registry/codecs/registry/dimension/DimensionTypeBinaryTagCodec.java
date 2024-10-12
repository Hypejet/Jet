package net.hypejet.jet.server.registry.codecs.registry.dimension;

import net.hypejet.jet.data.model.api.number.IntegerProvider;
import net.hypejet.jet.data.model.api.registry.registries.dimension.DimensionType;
import net.hypejet.jet.server.nbt.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.PackedIdentifierBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.identifier.TagIdentifierBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.number.IntegerProviderBinaryTagCodec;
import net.hypejet.jet.server.util.BinaryTagUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain BinaryTagCodec binary tag codec} which deserializes and serializes
 * a {@linkplain DimensionType dimension type}.
 *
 * @since 1.0
 * @author Codestech
 * @see DimensionType
 * @see BinaryTagCodec
 */
public final class DimensionTypeBinaryTagCodec implements BinaryTagCodec<DimensionType> {

    private static final DimensionTypeBinaryTagCodec INSTANCE = new DimensionTypeBinaryTagCodec();

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

    private DimensionTypeBinaryTagCodec() {}

    @Override
    public @NonNull DimensionType read(@NonNull BinaryTag tag) {
        if (!(tag instanceof CompoundBinaryTag compound))
            throw new IllegalArgumentException("The binary tag specified must be a compound binary tag");

        DimensionType.Builder builder = DimensionType.builder();

        BinaryTag fixedTimeBinaryTag = compound.get(FIXED_TIME);
        if (fixedTimeBinaryTag instanceof NumberBinaryTag fixedTimeIntBinaryTag)
            builder.fixedTime(fixedTimeIntBinaryTag.longValue());

        Key infiniburn = BinaryTagUtil.readOptional(INFINIBURN, compound, TagIdentifierBinaryTagCodec.instance());
        Key effects = BinaryTagUtil.readOptional(EFFECTS, compound, PackedIdentifierBinaryTagCodec.instance());
        IntegerProvider monsterSpawnLightLevel = BinaryTagUtil.readOptional(MONSTER_SPAWN_LIGHT_LEVEL, compound,
                IntegerProviderBinaryTagCodec.instance());

        if (infiniburn == null)
            throw new IllegalArgumentException("The infiniburn was not specified");
        if (effects == null)
            throw new IllegalArgumentException("The effects were not specified");
        if (monsterSpawnLightLevel == null)
            throw new IllegalArgumentException("The monster spawn light level was not specified");

        return builder.hasSkylight(compound.getBoolean(HAS_SKYLIGHT))
                .hasCeiling(compound.getBoolean(HAS_CEILING))
                .ultrawarm(compound.getBoolean(ULTRAWARM))
                .natural(compound.getBoolean(NATURAL))
                .coordinateScale(compound.getDouble(COORDINATE_SCALE))
                .bedWorks(compound.getBoolean(BED_WORKS))
                .respawnAnchorWorks(compound.getBoolean(RESPAWN_ANCHOR_WORKS))
                .minY(compound.getInt(MIN_Y))
                .height(compound.getInt(HEIGHT))
                .localHeight(compound.getInt(LOGICAL_HEIGHT))
                .infiniburn(infiniburn)
                .effects(effects)
                .ambientLight(compound.getFloat(AMBIENT_LIGHT))
                .piglinSafe(compound.getBoolean(PIGLIN_SAFE))
                .hasRaids(compound.getBoolean(HAS_RAIDS))
                .monsterSpawnLightLevel(monsterSpawnLightLevel)
                .monsterSpawnBlockLightLimit(compound.getInt(MONSTER_SPAWN_BLOCK_LIGHT_LIMIT))
                .build();
    }

    @Override
    public @NonNull BinaryTag write(@NonNull DimensionType object) {
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
                .put(INFINIBURN, TagIdentifierBinaryTagCodec.instance().write(object.infiniburn()))
                .put(EFFECTS, PackedIdentifierBinaryTagCodec.instance().write(object.effects()))
                .putFloat(AMBIENT_LIGHT, object.ambientLight())
                .putBoolean(PIGLIN_SAFE, object.piglinSafe())
                .putBoolean(HAS_RAIDS, object.hasRaids())
                .put(MONSTER_SPAWN_LIGHT_LEVEL,
                        IntegerProviderBinaryTagCodec.instance().write(object.monsterSpawnLightLevel()))
                .putInt(MONSTER_SPAWN_BLOCK_LIGHT_LIMIT, object.monsterSpawnBlockLightLimit())
                .build();
    }

    /**
     * Gets an instance of the {@linkplain DimensionTypeBinaryTagCodec dimension type binary tag codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull DimensionTypeBinaryTagCodec instance() {
        return INSTANCE;
    }
}