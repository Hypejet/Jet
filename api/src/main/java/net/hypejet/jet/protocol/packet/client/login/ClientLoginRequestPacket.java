package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@linkplain ClientPacket server-bound packet} that requests a server to send a login data.
 *
 * @since 1.0
 * @author Codestech
 */
public interface ClientLoginRequestPacket extends ClientPacket {
    /**
     * Gets a username of a player that sends the packet.
     *
     * @return the username
     * @since 1.0
     */
    @NonNull String username();

    /**
     * Gets a {@linkplain UUID unique identifier} of a player that sends the packet.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();
}