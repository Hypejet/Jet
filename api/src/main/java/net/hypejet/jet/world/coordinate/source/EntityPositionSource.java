package net.hypejet.jet.world.coordinate.source;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.UUID;

/**
 * A {@linkplain PositionSource position source} providing {@linkplain Position position}
 * of the specified {@linkplain Entity entity} as a {@linkplain Vector vector} with the specified
 * {@code Y} offset added to the {@code Y} axis value of the final vector.
 *
 * @param uniqueId unique identifier of the entity whose position should be provided
 * @param yOffset the {@code Y} offset added to the position when it is being provided by this position source
 * @since 1.0
 * @see Position
 * @see Entity
 * @see PositionSource
 */
@NullMarked
public record EntityPositionSource(UUID uniqueId, float yOffset) implements PositionSource {
    /**
     * Constructs the {@linkplain EntityPositionSource entity position source}.
     *
     * @param entity the entity whose position should be provided
     * @param yOffset the {@code Y} offset to add to the position when it is
     *                going to be provided by the position source in construction
     * @since 1.0
     */
    public EntityPositionSource(Entity entity, float yOffset) {
        this(entity.uniqueId(), yOffset);
    }

    /**
     * Constructs the {@linkplain EntityPositionSource entity position source}.
     *
     * @param uniqueId unique identifier of the entity whose position should be provided
     * @param yOffset the {@code Y} offset to add to the position when it is
     *                going to be provided by the position source in construction
     * @since 1.0
     */
    public EntityPositionSource {
        Objects.requireNonNull(uniqueId, "unique id");
    }
}