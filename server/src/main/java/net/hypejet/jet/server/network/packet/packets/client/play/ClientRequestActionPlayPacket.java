package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ClientPacket a client packet}, which requests a server to perform an action specified.
 *
 * @param action the action
 * @since 1.0
 * @see ClientPacket
 */
public record ClientRequestActionPlayPacket(@NonNull Action action) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientRequestActionPlayPacket client request action play packet}.
     *
     * @param action the action
     * @since 1.0
     */
    public ClientRequestActionPlayPacket {
        Objects.requireNonNull(action, "action");
    }

    /**
     * Represents an action of {@linkplain ClientRequestActionPlayPacket a request action play packet}.
     *
     * @since 1.0
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