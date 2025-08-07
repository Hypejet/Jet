package net.hypejet.jet.entity.movement.acquisition;

import net.hypejet.concurrency.Acquisition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain Acquisition an acquisition} of {@linkplain Position a position} and a delta movement
 * of {@linkplain net.hypejet.jet.entity.Entity an entity}.
 *
 * @since 1.0
 * @see Acquisition
 * @see Position
 * @see net.hypejet.jet.entity.Entity
 */
public interface MovementAcquisition extends Acquisition {
    /**
     * Gets {@linkplain Position a position} of {@linkplain net.hypejet.jet.entity.Entity an entity} associated
     * with this acquisition.
     *
     * @return the position
     * @since 1.0
     */
    @NonNull Position position();

    /**
     * Gets {@linkplain Vector a vector} of a delta movement of {@linkplain net.hypejet.jet.entity.Entity an entity}
     * associated with this acquisition.
     *
     * @return the vector
     * @since 1.0
     */
    @NonNull Vector deltaMovement();
}