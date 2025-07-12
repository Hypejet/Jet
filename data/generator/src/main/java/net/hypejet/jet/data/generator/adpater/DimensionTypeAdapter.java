package net.hypejet.jet.data.generator.adpater;

import net.hypejet.jet.data.json.model.type.dimension.JsonDimensionType;
import net.minecraft.world.level.dimension.DimensionType;
import org.jspecify.annotations.NonNull;

import java.util.OptionalLong;

/**
 * Represents something converting {@linkplain DimensionType dimension types} to a Jet data equivalent.
 *
 * @since 1.0
 * @see DimensionType
 */
public final class DimensionTypeAdapter {

    private DimensionTypeAdapter() {}

    /**
     * Converts the specified {@linkplain DimensionType dimension type} to a Jet data equivalent.
     *
     * @param type the dimension type to convert
     * @return the converted dimension type
     * @since 1.0
     */
    public static @NonNull JsonDimensionType convert(@NonNull DimensionType type) {
        OptionalLong fixedTime = type.fixedTime();
        return new JsonDimensionType(
                fixedTime.isPresent() ? fixedTime.getAsLong() : null,
                type.hasSkyLight(),
                type.hasCeiling(),
                type.ultraWarm(),
                type.natural(),
                type.coordinateScale(),
                type.bedWorks(),
                type.respawnAnchorWorks(),
                type.minY(),
                type.height(),
                type.logicalHeight(),
                KeyAdapter.convert(type.infiniburn().location()),
                KeyAdapter.convert(type.effectsLocation()),
                type.ambientLight(),
                type.cloudHeight().orElse(null),
                convertMonsterSettings(type.monsterSettings())
        );
    }

    private static JsonDimensionType.@NonNull MonsterSettings convertMonsterSettings(
            DimensionType.@NonNull MonsterSettings monsterSettings
    ) {
        return new JsonDimensionType.MonsterSettings(
                monsterSettings.piglinSafe(),
                monsterSettings.hasRaids(),
                IntProviderAdapter.convert(monsterSettings.monsterSpawnLightTest()),
                monsterSettings.monsterSpawnBlockLightLimit()
        );
    }
}