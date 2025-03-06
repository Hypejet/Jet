package net.hypejet.jet.event.events.ping;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.PlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a client associated with {@linkplain PlayerConnection a player connection}
 * responds to a ping request.
 *
 * @param connection a player connection that respond to the ping
 * @param pingIdentifier an identifier of the ping that the player connection respond to
 * @since 1.0
 */
public record PongEvent(@NonNull PlayerConnection connection, int pingIdentifier) {
    /**
     * Constructs the {@linkplain PongEvent pong event}.
     *
     * @param connection a player connection that respond to the ping
     * @param pingIdentifier an identifier of the ping that the player connection respond to
     * @since 1.0
     */
    public PongEvent {
        NullabilityUtil.requireNonNull(connection, "connection");
    }
}