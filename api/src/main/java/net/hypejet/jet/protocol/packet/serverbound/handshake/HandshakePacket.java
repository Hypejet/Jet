package net.hypejet.jet.protocol.packet.serverbound.handshake;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ServerBoundPacket server-bound packet} that initializes connection of a player.
 *
 * @param protocolVersion a version of the Minecraft protocol
 * @param serverAddress an address of the server
 * @param serverPort a port of the server
 * @param nextState a next protocol state, which the connection will switch to
 * @since 1.0
 * @author Codestech
 */
public record HandshakePacket(int protocolVersion, @NonNull String serverAddress,
                              int serverPort, @NonNull ProtocolState nextState) implements ServerBoundPacket {}