package net.hypejet.jet.data.json.model.position;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A global position of a block.
 *
 * @param dimensionKey the key of dimension that the block is in
 * @param position the position of the block in the specified dimension
 * @since 1.0
 */
@NullMarked
public record JsonGlobalPosition(Key dimensionKey, JsonBlockPosition position) {
    /**
     * Constructs the {@linkplain JsonGlobalPosition global position}.
     *
     * @param dimensionKey the key of dimension that the block which should be
     *                     represented by the constructed global position is in
     * @param position the position of the block that should be represented by the constructed global position is in
     * @since 1.0
     */
    public JsonGlobalPosition {
        Objects.requireNonNull(dimensionKey, "dimension type");
        Objects.requireNonNull(position, "position");
    }
}