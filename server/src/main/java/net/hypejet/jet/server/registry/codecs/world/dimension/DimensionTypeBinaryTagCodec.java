package net.hypejet.jet.server.registry.codecs.world.dimension;

import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.util.codec.BinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.util.game.number.IntProviderBinaryTagCodec;
import net.hypejet.jet.world.dimension.DimensionType;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.LongBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.booleanValue;
import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.optionalTag;
import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain BinaryTagCodec binary-tag codec} of {@linkplain DimensionType dimension types}.
 *
 * @since 1.0
 * @see DimensionType
 * @see BinaryTagCodec
 */
@NullMarked
public final class DimensionTypeBinaryTagCodec implements BinaryTagCodec<DimensionType> {

    private static final String FIXED_TIME_FIELD = "fixed_time";
    private static final String HAS_SKYLIGHT_FIELD = "has_skylight";
    private static final String HAS_CEILING_FIELD = "has_ceiling";
    private static final String ULTRA_WARM_FIELD = "ultrawarm";
    private static final String NATURAL_FIELD = "natural";
    private static final String COORDINATE_SCALE_FIELD = "coordinate_scale";
    private static final String BED_WORKS_FIELD = "bed_works";
    private static final String RESPAWN_ANCHOR_WORKS_FIELD = "respawn_anchor_works";
    private static final String MIN_Y_FIELD = "min_y";
    private static final String HEIGHT_FIELD = "height";
    private static final String LOGICAL_HEIGHT_FIELD = "logical_height";
    private static final String INFINIBURN_FIELD = "infiniburn";
    private static final String EFFECTS_FIELD = "effects";
    private static final String AMBIENT_LIGHT_FIELD = "ambient_light";
    private static final String CLOUD_HEIGHT_FIELD = "cloud_height";
    private static final String PIGLIN_SAFE_FIELD = "piglin_safe";
    private static final String HAS_RAIDS_FIELD = "has_raids";
    private static final String SPAWN_LIGHT_TEST_FIELD = "monster_spawn_light_level";
    private static final String SPAWN_BLOCK_LIGHT_LIMIT_FIELD = "monster_spawn_block_light_limit";

    /**
     * An instance of the {@linkplain DimensionTypeBinaryTagCodec dimension type binary-tag codec}.
     *
     * @since 1.0
     */
    public static final DimensionTypeBinaryTagCodec INSTANCE = new DimensionTypeBinaryTagCodec();

    private DimensionTypeBinaryTagCodec() {}

    @Override
    public DimensionType decode(BinaryTag binaryTag, JetMinecraftServer server) {
        if (binaryTag instanceof CompoundBinaryTag compound) {
            LongBinaryTag fixedTimeTag = optionalTag(FIXED_TIME_FIELD, compound, BinaryTagTypes.LONG);
            IntBinaryTag cloudHeightTag = optionalTag(CLOUD_HEIGHT_FIELD, compound, BinaryTagTypes.INT);
            return new DimensionType(
                    fixedTimeTag == null ? null : fixedTimeTag.value(),
                    booleanValue(requiredTag(HAS_SKYLIGHT_FIELD, compound, BinaryTagTypes.BYTE)),
                    booleanValue(requiredTag(HAS_CEILING_FIELD, compound, BinaryTagTypes.BYTE)),
                    booleanValue(requiredTag(ULTRA_WARM_FIELD, compound, BinaryTagTypes.BYTE)),
                    booleanValue(requiredTag(NATURAL_FIELD, compound, BinaryTagTypes.BYTE)),
                    requiredTag(COORDINATE_SCALE_FIELD, compound, BinaryTagTypes.DOUBLE).value(),
                    booleanValue(requiredTag(BED_WORKS_FIELD, compound, BinaryTagTypes.BYTE)),
                    booleanValue(requiredTag(RESPAWN_ANCHOR_WORKS_FIELD, compound, BinaryTagTypes.BYTE)),
                    requiredTag(MIN_Y_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(HEIGHT_FIELD, compound, BinaryTagTypes.INT).value(),
                    requiredTag(LOGICAL_HEIGHT_FIELD, compound, BinaryTagTypes.INT).value(),
                    KeyBinaryTagCodec.HASHED_INSTANCE.decode(requiredTag(INFINIBURN_FIELD, compound), server),
                    KeyBinaryTagCodec.INSTANCE.decode(requiredTag(EFFECTS_FIELD, compound), server),
                    requiredTag(AMBIENT_LIGHT_FIELD, compound, BinaryTagTypes.FLOAT).value(),
                    cloudHeightTag == null ? null : cloudHeightTag.value(),
                    new DimensionType.MonsterSettings(
                            booleanValue(requiredTag(PIGLIN_SAFE_FIELD, compound, BinaryTagTypes.BYTE)),
                            booleanValue(requiredTag(HAS_RAIDS_FIELD, compound, BinaryTagTypes.BYTE)),
                            IntProviderBinaryTagCodec.INSTANCE.decode(
                                    requiredTag(SPAWN_LIGHT_TEST_FIELD, compound),
                                    server
                            ),
                            requiredTag(SPAWN_BLOCK_LIGHT_LIMIT_FIELD, compound, BinaryTagTypes.INT).value()
                    )
            );
        } else {
            throw new IllegalArgumentException(
                    "The encoded tag must be of compound type to decode it to a dimension type"
            );
        }
    }

    @Override
    public BinaryTag encode(DimensionType value, JetMinecraftServer server) {
        DimensionType.MonsterSettings monsterSettings = value.monsterSettings();
        CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
                .putBoolean(HAS_SKYLIGHT_FIELD, value.hasSkyLight())
                .putBoolean(HAS_CEILING_FIELD, value.hasCeiling())
                .putBoolean(ULTRA_WARM_FIELD, value.ultraWarm())
                .putBoolean(NATURAL_FIELD, value.natural())
                .putDouble(COORDINATE_SCALE_FIELD, value.coordinateScale())
                .putBoolean(BED_WORKS_FIELD, value.bedWorks())
                .putBoolean(RESPAWN_ANCHOR_WORKS_FIELD, value.respawnAnchorWorks())
                .putInt(MIN_Y_FIELD, value.minY())
                .putInt(HEIGHT_FIELD, value.height())
                .putInt(LOGICAL_HEIGHT_FIELD, value.logicalHeight())
                .put(INFINIBURN_FIELD, KeyBinaryTagCodec.HASHED_INSTANCE.encode(value.infiniburn(), server))
                .put(EFFECTS_FIELD, KeyBinaryTagCodec.INSTANCE.encode(value.effects(), server))
                .putFloat(AMBIENT_LIGHT_FIELD, value.ambientLight())
                .putBoolean(PIGLIN_SAFE_FIELD, monsterSettings.piglinSafe())
                .putBoolean(HAS_RAIDS_FIELD, monsterSettings.hasRaids())
                .put(
                        SPAWN_LIGHT_TEST_FIELD,
                        IntProviderBinaryTagCodec.INSTANCE.encode(monsterSettings.spawnLightTest(), server)
                )
                .putInt(SPAWN_BLOCK_LIGHT_LIMIT_FIELD, monsterSettings.spawnBlockLightLimit());

        Long fixedTime = value.fixedTime();
        if (fixedTime != null) {
            builder.putLong(FIXED_TIME_FIELD, fixedTime);
        }

        Integer cloudHeight = value.cloudHeight();
        if (cloudHeight != null) {
            builder.putInt(CLOUD_HEIGHT_FIELD, cloudHeight);
        }

        return builder.build();
    }
}