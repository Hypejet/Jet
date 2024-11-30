package net.hypejet.jet.network.packet.client.login;

import net.hypejet.jet.network.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents {@linkplain ClientPacket a client packet} that provides a data of a player of the client to a server.
 *
 * @param username a username of the player
 * @param uniqueId a unique identifier of the player
 * @since 1.0
 * @author Codestech
 */
public record ClientLoginRequestLoginPacket(@NonNull String username, @NonNull UUID uniqueId)
        implements ClientPacket {}