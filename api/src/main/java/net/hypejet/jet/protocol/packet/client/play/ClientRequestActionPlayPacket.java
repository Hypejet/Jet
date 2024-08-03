package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which requests a server to perform an action
 * specified.
 *
 * @param action the action
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientRequestActionPlayPacket(@NonNull Action action) implements ClientPlayPacket {
    /**
     * Represents an action of a {@linkplain ClientRequestActionPlayPacket request action play packet}.
     *
     * @since 1.0
     * @author Codestech
     * @see ClientRequestActionPlayPacket
     */
    public enum Action {
        /**
         * An action, which respawns a player.
         *
         * @since 1.0
         */
        PERFORM_RESPAWN,
        /**
         * An action, which sends statistics to a player.
         *
         * @since 1.0
         */
        REQUEST_STATS
    }
}