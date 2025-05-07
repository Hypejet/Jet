package net.hypejet.jet.world.block;

import net.hypejet.jet.registry.RegistryEntry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents a variant of a Minecraft block.
 *
 * @since 1.0
 */
public interface BlockState {
    /**
     * Gets {@linkplain RegistryEntry a registry entry} of {@linkplain BlockType a block type}
     * that this block state belongs to.
     *
     * @return the block type
     * @since 1.0
     */
    @NonNull RegistryEntry<? extends BlockType> blockType();

    /**
     * Gets {@linkplain Map a map} of properties of this block state.
     *
     * @return the map
     * @since 1.0
     */
    @NonNull Map<String, String> properties();
}