package net.hypejet.jet.server.network.packet.packets.server.play;

import com.google.common.collect.Sets;
import net.hypejet.jet.data.model.api.coordinate.Vector;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

/**
 * Represents {@linkplain ServerPacket a server packet}, which synchronizes position of a player on a client.
 *
 * @param teleportId an identifier of the teleportation (synchronization)
 * @param position the correct position of the player
 * @param deltaMovement the correct delta movement of the player
 * @param yaw the correct yaw of the player
 * @param pitch the correct pitch of the player
 * @param relativeFlags flags, which indicate whether a specific value of the position is relative to previous position
 * @since 1.0
 */
public record ServerSynchronizePositionPlayPacket(int teleportId, @NonNull Vector position,
                                                  @NonNull Vector deltaMovement, float yaw, float pitch,
                                                  @NonNull Set<RelativeFlag> relativeFlags)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerSynchronizePositionPlayPacket server synchronize position play packet}.
     *
     * @param teleportId an identifier of the teleportation (synchronization)
     * @param position the correct position of the player
     * @param deltaMovement the correct delta movement of the player
     * @param yaw the correct yaw of the player
     * @param pitch the correct pitch of the player
     * @param relativeFlags flags, which indicate whether a specific value of the position is relative to previous
     *                      position
     * @since 1.0
     */
    public ServerSynchronizePositionPlayPacket {
        NullabilityUtil.requireNonNull(position, "position");
        NullabilityUtil.requireNonNull(deltaMovement, "delta movement");
        relativeFlags = Sets.immutableEnumSet(NullabilityUtil.requireNonNull(relativeFlags, "relative flags"));
    }

    /**
     * Represents a flag indicating that a value of a position change is relative to previous position.
     *
     * @since 1.0
     */
    public enum RelativeFlag {
        /**
         * A relative flag indicating that an {@code X} value of a position change is relative.
         *
         * @since 1.0
         */
        RELATIVE_X,
        /**
         * A relative flag indicating that an {@code Y} value of a position change is relative.
         *
         * @since 1.0
         */
        RELATIVE_Y,
        /**
         * A relative flag indicating that an {@code Z} value of a position change is relative.
         *
         * @since 1.0
         */
        RELATIVE_Z,
        /**
         * A relative flag indicating that a pitch of a position change is relative.
         *
         * @since 1.0
         */
        RELATIVE_PITCH,
        /**
         * A relative flag indicating that a yaw value of a position change is relative.
         *
         * @since 1.0
         */
        RELATIVE_YAW,
        /**
         * A relative flag indicating that a delta {@code X} value of a position change is relative.
         *
         * @since 1.0
         */
        RELATIVE_DELTA_X,
        /**
         * A relative flag indicating that a delta {@code Y} value of a position change is relative.
         *
         * @since 1.0
         */
        RELATIVE_DELTA_Y,
        /**
         * A relative flag indicating that a delta {@code Z} value of a position change is relative.
         *
         * @since 1.0
         */
        RELATIVE_DELTA_Z,
        /**
         * A relative flag indicating that delta values of a position change should be updated with the new rotation
         * values.
         *
         * @since 1.0
         */
        ROTATE_DELTA
    }
}