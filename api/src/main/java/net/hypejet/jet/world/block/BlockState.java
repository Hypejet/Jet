package net.hypejet.jet.world.block;

import net.hypejet.jet.registry.holder.Holder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents a variant of a Minecraft block.
 *
 * @since 1.0
 */
public interface BlockState {
    /**
     * Gets a {@linkplain Holder holder} of a {@linkplain BlockType block type} that this block state belongs to.
     *
     * @return the block type holder
     * @since 1.0
     */
    @NonNull Holder<BlockType> blockType();

    /**
     * Gets a {@linkplain Map map} of properties of this block state.
     *
     * @return the map
     * @since 1.0
     */
    @NonNull Map<String, String> properties();
}