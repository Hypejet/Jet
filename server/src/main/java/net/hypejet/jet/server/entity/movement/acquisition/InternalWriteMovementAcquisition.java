package net.hypejet.jet.server.entity.movement.acquisition;

import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.entity.movement.acquisition.WriteMovementAcquisition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain WriteMovementAcquisition a write movement acquisition} that allows doing operations, which
 * should be allowed only for internal server behaviour.
 *
 * @since 1.0
 */
public interface InternalWriteMovementAcquisition extends WriteMovementAcquisition {
    /**
     * Sets {@linkplain Position a position} of {@linkplain net.hypejet.jet.entity.Entity an entity} associated
     * with this acquisition without sending updates to a client associated with the entity if it
     * is {@linkplain net.hypejet.jet.server.entity.player.JetPlayer a player}.
     *
     * @param position a value that the position should be set to
     * @since 1.0
     */
    void setPosition(@NonNull Position position);
}