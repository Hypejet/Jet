package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.data.model.coordinate.Position;
import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which synchronizes position of a player on client.
 *
 * @param position the correct position of the player
 * @param relativeFlags flags, which define whether a specific value of the position is relative
 * @param teleportId an identifier of the teleportation
 * @since 1.0
 * @author Codsetech
 */
public record ServerSynchronizePositionPlayPacket(@NonNull Position position,
                                                  @NonNull Collection<RelativeFlag> relativeFlags,
                                                  int teleportId) implements ServerPlayPacket {
    /**
     * Constructs the {@linkplain ServerSynchronizePositionPlayPacket synchronize position play packet}.
     *
     * @param position the correct position of the player
     * @param relativeFlags flags, which define whether a specific value of the position is relative
     * @since 1.0
     */
    public ServerSynchronizePositionPlayPacket {
        relativeFlags = Set.copyOf(relativeFlags);
    }

    /**
     * Represents a flag defining that a position of a value is relative.
     *
     * @since 1.0
     * @author Codestech
     */
    public enum RelativeFlag {
        /**
         * A relative flag defining that an {@code X} value of a position is relative.
         *
         * @since 1.0
         */
        RELATIVE_X,
        /**
         * A relative flag defining that an {@code Y} value of a position is relative.
         *
         * @since 1.0
         */
        RELATIVE_Y,
        /**
         * A relative flag defining that an {@code Z} value of a position is relative.
         *
         * @since 1.0
         */
        RELATIVE_Z,
        /**
         * A relative flag defining that a pitch of a position is relative.
         *
         * @since 1.0
         */
        RELATIVE_PITCH,
        /**
         * A relative flag defining that a yaw value of a position is relative.
         *
         * @since 1.0
         */
        RELATIVE_YAW
    }
}