package net.hypejet.jet.session;

import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginSuccessLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a session, during which a player is authenticated.
 * <p>A {@link #finish(String, UUID, Collection)} method should be called when the session is finished.</p>
 *
 * @since 1.0
 * @author Codestech
 */
public interface LoginSession {
    /**
     * Gets a connection with the player.
     *
     * @return the connection
     * @since 1.0
     */
    @NonNull PlayerConnection connection();

    /**
     * Finishes the session.
     *
     * @param username a username that the player should have
     * @param uniqueId a unique identifier that the player should have
     * @param properties a properties of the login
     * @since 1.0
     * @throws IllegalArgumentException if the session is already finished
     */
    void finish(@NonNull String username, @NonNull UUID uniqueId,
                @NonNull Collection<ServerLoginSuccessLoginPacket.Property> properties);
}