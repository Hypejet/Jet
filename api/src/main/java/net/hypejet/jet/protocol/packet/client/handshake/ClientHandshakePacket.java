package net.hypejet.jet.protocol.packet.client.handshake;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet} that initializes connection of a player.
 *
 * @param protocolVersion a version of Minecraft protocol that the client uses
 * @param serverAddress an address of a server that the client tries to connect to
 * @param serverPort a port of a server that the client tries to connect to
 * @param nextState a next {@linkplain ProtocolState protocol state} that the client will switch to
 * @since 1.0
 * @author Codestech
 */
public record ClientHandshakePacket(int protocolVersion, @NonNull String serverAddress, int serverPort,
                                    @NonNull ProtocolState nextState) implements ClientPacket {}