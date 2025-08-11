package net.hypejet.jet.server.world.chunk.heightmap;

import net.hypejet.jet.server.world.block.JetBlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A type of {@linkplain HeightMap height map}.
 *
 * @since 1.0
 */
public enum HeightMapType {
    /**
     * A {@linkplain HeightMapType height-map type} whose heightmaps store the highest height at which
     * a {@linkplain JetBlockState block state} is not an air.
     *
     * @since 1.0
     */
    WORLD_SURFACE(1) {
        @Override
        public boolean isOpaque(@NonNull JetBlockState state) {
            return !state.isAir();
        }
    },

    /**
     * A {@linkplain HeightMapType height-map type} whose heightmaps store the highest height at which
     * a {@linkplain JetBlockState block state} is either a fluid or blocks motion and is not leaves.
     *
     * @since 1.0
     */
    MOTION_BLOCKING(4) {
        @Override
        public boolean isOpaque(@NonNull JetBlockState state) {
            return state.hasFluidState() || state.blocksMotion();
        }
    },

    /**
     * A {@linkplain HeightMapType height map type} whose heightmaps store the highest height at which
     * a {@linkplain JetBlockState block state} is either a fluid or blocks motion.
     *
     * @since 1.0
     */
    MOTION_BLOCKING_NO_LEAVES(5) {
        @Override
        public boolean isOpaque(@NonNull JetBlockState state) {
            return MOTION_BLOCKING.isOpaque(state) && !state.isLeaves();
        }
    };

    private final int identifier;

    /**
     * Constructs the {@linkplain HeightMapType height map type}.
     *
     * @param identifier an identifier that the height map should have, used in serialization
     * @since 1.0
     */
    HeightMapType(int identifier) {
        this.identifier = identifier;
    }

    /**
     * Gets an identifier of this {@linkplain HeightMapType height-map type} that should be used in serialization.
     *
     * @return the height-map type identifier
     * @since 1.0
     */
    public int identifier() {
        return this.identifier;
    }

    /**
     * Gets whether the specified {@linkplain JetBlockState block state}
     * can be stored in {@linkplain HeightMap height maps} with this type.
     *
     * @param state the block state
     * @return {@code true} if the specified block state can be used to be stored
     *          in height maps of this type, {@code false} otherwise
     * @since 1.0
     */
    public abstract boolean isOpaque(@NonNull JetBlockState state);
}