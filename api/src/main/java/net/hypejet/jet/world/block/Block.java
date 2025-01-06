package net.hypejet.jet.world.block;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a Minecraft block.
 *
 * @since 1.0
 */
public interface Block {
    /**
     * Gets a default {@linkplain BlockState block state} of this block.
     *
     * @return the default block state
     * @since 1.0
     */
    @NotNull BlockState defaultBlockState();
}