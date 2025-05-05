package net.hypejet.jet.server.test.world.chunk;

import net.hypejet.jet.data.model.api.number.IntegerProvider;
import net.hypejet.jet.data.model.api.registries.dimension.DimensionType;
import net.kyori.adventure.key.Key;

/**
 * Represents a holder of {@linkplain DimensionType a dimension type} which can be used for tests related to chunks.
 *
 * @since 1.0
 * @see DimensionType
 */
public final class ChunkTestDimensionHolder {
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

    private ChunkTestDimensionHolder() {}
}