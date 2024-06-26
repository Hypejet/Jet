package net.hypejet.jet.event.events.ping;

import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an event, which is called when a client pings the server list.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerListPing
 */
public final class ServerListPingEvent {

    private final PlayerConnection connection;
    private ServerListPing ping;

    /**
     * Constructs the {@linkplain ServerListPingEvent server list ping event}.
     *
     * @param connection a player connection with the client, which pings the list
     * @param ping a ping response data
     * @since 1.0
     */
    public ServerListPingEvent(@NonNull PlayerConnection connection, @NonNull ServerListPing ping) {
        this.connection = connection;
        this.ping = ping;
    }

    /**
     * Gets a player connection with the client, which pings the list.
     *
     * @return the player connection
     * @since 1.0
     */
    public @NonNull PlayerConnection getConnection() {
        return this.connection;
    }

    /**
     * Gets a ping response data.
     *
     * @return the ping response data
     * @since 1.0
     */
    public @NonNull ServerListPing getPing() {
        return this.ping;
    }

    /**
     * Sets a ping response data.
     *
     * @param ping the ping response data
     * @since 1.0
     */
    public void setPing(@NonNull ServerListPing ping) {
        this.ping = ping;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerListPingEvent event)) return false;
        return Objects.equals(this.connection, event.connection) && Objects.equals(this.ping, event.ping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.connection, this.ping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ServerListPingEvent{" +
                "connection=" + this.connection +
                ", ping=" + this.ping +
                '}';
    }
}
