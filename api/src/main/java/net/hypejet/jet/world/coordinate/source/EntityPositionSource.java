package net.hypejet.jet.world.coordinate.source;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain PositionSource position source} providing {@linkplain Position position}
 * of the specified {@linkplain Entity entity} as a {@linkplain Vector vector} with the specified
 * {@code Y} offset added to the {@code Y} axis value of the final vector.
 *
 * @param entity the entity whose position should be provided
 * @param yOffset the {@code Y} offset
 * @since 1.0
 * @see Position
 * @see Entity
 * @see PositionSource
 */
@NullMarked
public record EntityPositionSource(Entity entity, float yOffset) implements PositionSource {
    @Override
    public Vector position() {
        return Vector.from(this.entity.position()).add(0, this.yOffset, 0);
    }
}