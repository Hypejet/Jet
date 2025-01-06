package net.hypejet.jet.world.block;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a state of {@linkplain Block a block}.
 *
 * @since 1.0
 * @see Block
 */
public interface BlockState {
    /**
     * Gets {@linkplain Key a key} of {@linkplain Block a block} that owns this block state.
     *
     * @return the key
     * @since 1.0
     */
    @NonNull Key blockKey();

    /**
     * Gets whether this block state should be recognised as an air.
     *
     * @return {@code} true if this block state should be recognised as an air, {@code false} otherwise
     * @since 1.0
     */
    boolean isAir();
}