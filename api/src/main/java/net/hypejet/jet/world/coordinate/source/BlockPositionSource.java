package net.hypejet.jet.world.coordinate.source;

import net.hypejet.jet.world.coordinate.BlockPosition;
import net.hypejet.jet.world.coordinate.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain PositionSource position source} providing a {@linkplain Vector vector}
 * representing a center position of a block at the specified {@linkplain BlockPosition block position}.
 *
 * @param blockPosition the block position whose center position should be provided
 * @since 1.0
 * @see BlockPosition
 * @see PositionSource
 */
@NullMarked
public record BlockPositionSource(BlockPosition blockPosition) implements PositionSource {
    @Override
    public Vector position() {
        return Vector.from(this.blockPosition).add(0.5, 0, 0.5);
    }
}