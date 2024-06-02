package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet} that requests a server to send a login data.
 *
 * @since 1.0
 * @author Codestech
 */
public non-sealed interface ClientLoginRequestPacket extends ClientLoginPacket {
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