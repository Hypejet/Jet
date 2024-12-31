package net.hypejet.jet.event.events.serverlist;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.util.game.ping.ServerListPing;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an event, which is called when a client pings requests data of the server that should be displayed on
 * the server list.
 *
 * @since 1.0
 * @see ServerListPing
 */
public final class ServerListPingEvent {

    private final PlayerConnection connection;
    private ServerListPing ping;

    /**
     * Constructs the {@linkplain ServerListPingEvent server list ping event}.
     *
     * @param connection a player connection associated with the client
     * @param ping a ping response data
     * @since 1.0
     */
    public ServerListPingEvent(@NonNull PlayerConnection connection, @NonNull ServerListPing ping) {
        this.connection = NullabilityUtil.requireNonNull(connection, "connection");
        this.ping = NullabilityUtil.requireNonNull(ping, "ping");
    }

    /**
     * Gets a player connection associated with the client.
     *
     * @return the player connection
     * @since 1.0
     */
    public @NonNull PlayerConnection connection() {
        return this.connection;
    }

    /**
     * Gets data that the server should respond with.
     *
     * @return the data
     * @since 1.0
     */
    public @NonNull ServerListPing getPing() {
        return this.ping;
    }

    /**
     * Sets data the server should respond with.
     *
     * @param ping a value to replace the data with
     * @since 1.0
     */
    public void setPing(@NonNull ServerListPing ping) {
        this.ping = NullabilityUtil.requireNonNull(ping, "ping");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ServerListPingEvent event)) return false;
        return Objects.equals(this.connection, event.connection) && Objects.equals(this.ping, event.ping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.connection, this.ping);
    }

    @Override
    public String toString() {
        return "ServerListPingEvent{" +
                "connection=" + this.connection +
                ", ping=" + this.ping +
                '}';
    }
}
