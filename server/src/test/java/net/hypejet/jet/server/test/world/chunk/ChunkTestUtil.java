package net.hypejet.jet.server.test.world.chunk;

import net.hypejet.jet.data.model.api.color.Color;
import net.hypejet.jet.data.model.api.number.IntegerProvider;
import net.hypejet.jet.data.model.api.registries.biome.Biome;
import net.hypejet.jet.data.model.api.registries.biome.effects.BiomeEffectSettings;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.hypejet.jet.server.world.block.JetBlockState;
import net.hypejet.jet.server.world.block.JetBlockType;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Set;

/**
 * Represents a set of utilities that are useful for tests related
 * to {@linkplain net.hypejet.jet.server.world.chunk.JetChunk chunks}.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.JetChunk
 */
public final class ChunkTestUtil {
    /**
     * {@linkplain DimensionType A dimension type} which can be used for tests related to chunks.
     *
     * @since 1.0
     */
    public static final DimensionType DIMENSION_TYPE = new DimensionType(
            null, true, false, false, true, 1,
            true, true, -64, 384, 384,
            Key.key("infiniburn_overworld"), Key.key("overworld"), 0, false,
            true, new IntegerProvider.Uniform(0, 7), 0
    );

    private ChunkTestUtil() {}

    /**
     * Creates a mockup {@linkplain JetBlockState block state} without optional data.
     *
     * @param key a key of a block type that the block state should have
     * @param isAir whether the block state should be recognised as an air
     * @param hasFluidState whether the block state should be recognised as a block state that has a fluid state
     *                      associated with it
     * @param blockMotion whether the block state should be recognised as a block state that blocks motion
     * @return the block state
     * @since 1.0
     */
    public static @NonNull JetBlockState createMockupBlockState(@NonNull Key key, boolean isAir,
                                                                boolean hasFluidState, boolean blockMotion) {
        return new JetBlockState(
                new JetRegistryEntry<>(key, new JetBlockType(Set.of(), null), null),
                Map.of(), isAir, hasFluidState, blockMotion
        );
    }


    /**
     * Creates {@linkplain JetRegistryEntry a registry entry} containing a mockup {@linkplain Biome biome}
     * without optional data.
     *
     * @param key a key that the registry entry should have
     * @return the registry entry
     * @since 1.0
     */
    public static @NonNull JetRegistryEntry<Biome> createMockupBiome(@NonNull Key key) {
        Color zeroColor = new Color(0);
        return new JetRegistryEntry<>(
                key,
                new Biome(
                        false, 0f, null, 0f,
                        new BiomeEffectSettings(
                                zeroColor, zeroColor, zeroColor, zeroColor, null, null,
                                null, null, null, null, null, null
                        )
                ),
                null
        );
    }
}