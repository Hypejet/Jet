package net.hypejet.jet.entity.movement.acquisition;

import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.coordinate.Vector;
import net.hypejet.jet.entity.movement.flag.RelativeFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents {@linkplain MovementAcquisition a movement acquisition}, which allows changing
 * the {@linkplain Position position} and the delta movement.
 *
 * @since 1.0
 * @see MovementAcquisition
 */
public interface WriteMovementAcquisition extends MovementAcquisition {
    /**
     * Sets a view of {@linkplain Position a position} of {@linkplain net.hypejet.jet.entity.Entity an entity}
     * associated with this acquisition, delta movement and other values of the {@linkplain Position position}
     * are kept.
     *
     * @param yaw a yaw that the position should have
     * @param pitch a pitch that the position should have
     * @since 1.0
     */
    void setView(float yaw, float pitch);

    /**
     * Teleports {@linkplain net.hypejet.jet.entity.Entity an entity} associated with this acquisition
     * to an absolute {@linkplain Position position} specified.
     *
     * <p>Note that delta movement of the entity is reset after this operation.</p>
     *
     * @param position the position that the entity should be teleported to
     * @since 1.0
     */
    void teleport(@NonNull Position position);

    /**
     * Updates {@linkplain Position a position} and {@linkplain Vector a vector} of a delta movement
     * of {@linkplain net.hypejet.jet.entity.Entity an entity} associated with this acquisition
     *
     * <p>Specifying any {@linkplain RelativeFlag relative flag} makes a specified value of a type associated with
     * the flag relative to a previous value, in other words it makes the value being summed up with a previous
     * one.</p>
     *
     * @param position a value that the position should be updated to
     * @param deltaMovement a value that a delta movement vector should be updated to
     * @param flags the relative flags
     * @since 1.0
     */
    void update(@NonNull Position position, @NonNull Vector deltaMovement, @NonNull RelativeFlag @NonNull ... flags);

    /**
     * Updates {@linkplain Position a position} and {@linkplain Vector a vector} of a delta movement
     * of {@linkplain net.hypejet.jet.entity.Entity an entity} associated with this acquisition
     *
     * <p>Specifying any {@linkplain RelativeFlag relative flag} makes a specified value of a type associated with
     * the flag relative to a previous value, in other words it makes the value being summed up with a previous
     * one.</p>
     *
     * @param position a value that the position should be updated to
     * @param deltaMovement a value that a delta movement vector should be updated to
     * @param flags the relative flags
     * @since 1.0
     */
    void update(@NonNull Position position, @NonNull Vector deltaMovement, @NonNull Collection<RelativeFlag> flags);
}