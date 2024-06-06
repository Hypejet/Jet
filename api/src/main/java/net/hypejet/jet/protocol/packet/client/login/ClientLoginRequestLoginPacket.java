package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet} that requests a server to send a login data.
 *
 * @param username a username of a player that sends the packet
 * @param uniqueId a {@linkplain UUID unique identifier} of a player that sends the packet
 * @since 1.0
 * @author Codestech
 */
public record ClientLoginRequestLoginPacket(@NonNull String username, @NonNull UUID uniqueId)
        implements ClientLoginPacket {}