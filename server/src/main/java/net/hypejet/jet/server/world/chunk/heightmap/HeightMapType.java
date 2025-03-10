package net.hypejet.jet.server.world.chunk.heightmap;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.data.model.server.registry.registries.block.state.BlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a type of {@linkplain HeightMap a height map}.
 *
 * @since 1.0
 */
public enum HeightMapType {
    /**
     * {@linkplain HeightMapType A height map type} whose heightmaps store the highest height, at which
     * {@linkplain BlockState a block state} is not an air.
     *
     * @since 1.0
     */
    WORLD_SURFACE("WORLD_SURFACE") {
        @Override
        public boolean isOpaque(@NonNull BlockState state) {
            return !state.isAir();
        }
    },

    /**
     * {@linkplain HeightMapType A height map type} whose heightmaps store the highest height, at which
     * {@linkplain BlockState a block state} either is a fluid or blocks motion.
     *
     * @since 1.0
     */
    MOTION_BLOCKING("MOTION_BLOCKING") {
        @Override
        public boolean isOpaque(@NonNull BlockState state) {
            return state.hasFluidState() || state.blocksMotion();
        }
    };

    private final String serializationName;

    /**
     * Constructs the {@linkplain HeightMapType height map type}.
     *
     * @param serializationName a name that should be used when serializing height maps with the type that
     *                          is being constructed
     * @since 1.0
     */
    HeightMapType(@NonNull String serializationName) {
        this.serializationName = NullabilityUtil.requireNonNull(serializationName, "serialization name");
    }

    /**
     * Gets a name that should be used when serializing {@linkplain HeightMap height maps} with this type.
     *
     * @return the name
     * @since 1.0
     */
    public @NonNull String serializationName() {
        return this.serializationName;
    }

    /**
     * Gets whether {@linkplain BlockState a block state} specified can be used to be stored
     * in {@linkplain HeightMap a height map} with this type.
     *
     * @param state the block state
     * @return {@code true} if the block state specified can be used to be stored in a height map of this type,
     *         {@code false} otherwise
     * @since 1.0
     */
    public abstract boolean isOpaque(@NonNull BlockState state);
}