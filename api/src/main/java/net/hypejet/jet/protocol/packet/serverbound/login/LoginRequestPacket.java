package net.hypejet.jet.protocol.packet.serverbound.login;

import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@link ServerBoundPacket server-bound packet} that requests a server to send a login data.
 *
 * @param username a username of a player that sends the packet
 * @param uniqueId a unique id of a player that sends the packet
 * @since 1.0
 * @author Codestech
 */
public record LoginRequestPacket(@NonNull String username, @NonNull UUID uniqueId)
        implements ServerBoundPacket {}