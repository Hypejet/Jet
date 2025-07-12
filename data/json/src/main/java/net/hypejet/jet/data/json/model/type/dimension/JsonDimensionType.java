package net.hypejet.jet.data.json.model.type.dimension;

import net.hypejet.jet.data.json.model.JsonIntProvider;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A type of Minecraft dimension.
 *
 * @param fixedTime the fixed daytime that worlds using this dimension type should have, {@code null} if these worlds
 *                  should have a normal day cycle
 * @param hasSkyLight whether worlds using this dimension type should have skylight
 * @param hasCeiling whether worlds using this dimension type have a bedrock ceiling
 * @param ultraWarm whether world using this dimension type should have nether-like water behaviour
 * @param natural when {@code true}, nether portals in worlds using this dimension type spawn zombified piglins
 *                and creaking hearts can spawn creakings, when {@code false}, compasses spin randomly and using
 *                a bed to set respawn point or sleep is disabled
 * @param coordinateScale the multiplier that should be applied to coordinates of players leaving worlds
 *                        using this dimension type
 * @param bedWorks when {@code false}, using the bed in worlds using this dimension type makes the used bed blow up
 * @param respawnAnchorWorks when {@code false}, using the respawn anchor in worlds using this dimension type
 *                           makes the used respawn anchor blow up
 * @param minY the minimum height in which blocks can exist within worlds using this dimension type
 * @param height the total height in which blocks can exist within worlds using this dimension type
 * @param localHeight the maximum height to which chorus fruits and nether portals can bring players within
 *                    worlds using this dimension type
 * @param infiniburn the key of block tag which should make fire on blocks using that tag burn infinitely
 * @param effects the key of the dimension effect that worlds using this dimension type should use
 * @param ambientLight how much light worlds using this dimension should have, where {@code 0} makes it completely
 *                     follow the light level and {@code 1} makes it disable the ambient lighting
 * @param cloudHeight determines the lower edge of the clouds that worlds using this dimension type should have,
 *                    {@code null} if clouds should be disabled in these worlds
 * @param monsterSettings behaviour settings that monsters in worlds using this dimension type should have
 * @since 1.0
 */
public record JsonDimensionType(@Nullable Long fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultraWarm,
                                boolean natural, double coordinateScale, boolean bedWorks, boolean respawnAnchorWorks,
                                int minY, int height, int localHeight, @NonNull Key infiniburn,
                                @NonNull Key effects, float ambientLight, @Nullable Integer cloudHeight,
                                @NonNull MonsterSettings monsterSettings) {
    /**
     * Constructs the {@linkplain JsonDimensionType dimension type}.
     *

     * @param fixedTime the fixed daytime that worlds using this dimension type should have, {@code null} if these
     *                  worlds should have a normal day cycle
     * @param hasSkyLight whether worlds using this dimension type should have skylight
     * @param hasCeiling whether worlds using this dimension type have a bedrock ceiling
     * @param ultraWarm whether world using this dimension type should have nether-like water behaviour
     * @param natural when {@code true}, nether portals in worlds using this dimension type spawn zombified piglins
     *                and creaking hearts can spawn creakings, when {@code false}, compasses spin randomly and using
     *                a bed to set respawn point or sleep is disabled
     * @param coordinateScale the multiplier that should be applied to coordinates of players leaving worlds
     *                        using this dimension type
     * @param bedWorks when {@code false}, using the bed in worlds using this dimension type makes the used bed blow up
     * @param respawnAnchorWorks when {@code false}, using the respawn anchor in worlds using this dimension type
     *                           makes the used respawn anchor blow up
     * @param minY the minimum height in which blocks can exist within worlds using this dimension type
     * @param height the total height in which blocks can exist within worlds using this dimension type
     * @param localHeight the maximum height to which chorus fruits and nether portals can bring players within
     *                    worlds using this dimension type
     * @param infiniburn the key of block tag which should make fire on blocks using that tag burn infinitely
     * @param effects the key of the dimension effect that worlds using this dimension type should use
     * @param ambientLight how much light worlds using this dimension should have, where {@code 0} makes it completely
     *                     follow the light level and {@code 1} makes it disable the ambient lighting
     * @param cloudHeight determines the lower edge of the clouds that worlds using this dimension type should have,
     *                    {@code null} if clouds should be disabled in these worlds
     * @param monsterSettings behaviour settings that monsters in worlds using this dimension type should have
     * @since 1.0
     */
    public JsonDimensionType {
        Objects.requireNonNull(infiniburn, "infiniburn");
        Objects.requireNonNull(effects, "effects");
        Objects.requireNonNull(monsterSettings, "monster settings");
    }

    /**
     * Represents monster settings of a {@linkplain JsonDimensionType dimension type}.
     *
     * @param piglinSafe when {@code false}, piglins and hoglins in worlds using the dimension type associated
     *                   with these monster settings transform to zombified entities
     * @param hasRaids whether players with the bad omen effect can cause a raid in world using the dimension type
     *                 associated with these monster settings
     * @param spawnLightTest the maximum light level value required to make monsters spawn in the dimension type
     *                       associated with these monster settings
     * @param spawnBlockLightLimit the maximum block light level value required to make monsters spawn in the dimension
     *                             type associated with these monster settings
     * @since 1.0
     */
    public record MonsterSettings(boolean piglinSafe, boolean hasRaids, @NonNull JsonIntProvider spawnLightTest,
                                  int spawnBlockLightLimit) {
        /**
         * Constructs the {@linkplain MonsterSettings monster settings}.
         *
         * @param piglinSafe when {@code false}, piglins and hoglins in worlds using the dimension type associated
         *                   with these monster settings transform to zombified entities
         * @param hasRaids whether players with the bad omen effect can cause a raid in world using the dimension type
         *                 associated with these monster settings
         * @param spawnLightTest the maximum light level value required to make monsters spawn in the dimension type
         *                       associated with these monster settings
         * @param spawnBlockLightLimit the maximum block light level value required to make monsters spawn
         *                             in the dimension type associated with these monster settings
         * @since 1.0
         */
        public MonsterSettings {
            Objects.requireNonNull(spawnLightTest, "spawn light test");
        }
    }
}