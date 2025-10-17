package net.hypejet.jet.world.block.state;

import net.hypejet.jet.registry.blockstate.BlockStateRegistry;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.world.block.BlockType;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.Objects;

/**
 * Something referencing to a {@linkplain BlockState block state}.
 *
 * @param blockType the block type whose block state this object references to
 * @param properties properties of the block state that this object references to
 * @since 1.0
 * @see BlockState
 */
@NullMarked
public record BlockStateReference(Holder.Reference<BlockType> blockType, Map<String, Object> properties) {
    /**
     * Constructs the {@linkplain BlockStateRegistry block state reference}.
     *
     * @param blockType the type of block whose block state should be referenced
     * @param properties properties of the block state that the constructed object should reference to
     * @since 1.0
     */
    public BlockStateReference {
        Objects.requireNonNull(blockType, "block type");
        properties = Map.copyOf(Objects.requireNonNull(properties, "properties"));
    }
}