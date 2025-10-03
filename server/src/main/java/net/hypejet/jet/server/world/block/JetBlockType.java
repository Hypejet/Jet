package net.hypejet.jet.server.world.block;

import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.world.block.BlockType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.Set;

/**
 * An implementation of the {@linkplain BlockType block type}.
 *
 * @param requiredFeatureFlags a set of feature flag keys required to enable this block type
 * @since 1.0
 */
@NullMarked
public record JetBlockType(Set<Key> requiredFeatureFlags) implements BlockType {
    /**
     * Constructs the {@linkplain JetBlockType block type implementation}.
     *
     * @param requiredFeatureFlags a set of feature flag keys that should be
     *                             required to enable the constructed block type
     * @since 1.0
     */
    public JetBlockType {
        Objects.requireNonNull(requiredFeatureFlags, "required feature flags");
    }

    /**
     * Converts the specified {@linkplain JsonBlock Jet data block} to a Jet equivalent.
     *
     * @param block the block to convert
     * @return the converted block
     * @since 1.0
     */
    public static JetBlockType convert(JsonBlock block) {
        Objects.requireNonNull(block, "block");
        return new JetBlockType(block.requiredFeatureFlags());
    }
}