package net.hypejet.jet.server.protocol.serverbound.handshake;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.handshake.HandshakePacket;
import org.checkerframework.checker.nullness.qual.NonNull;

public record HandshakePacketImpl(int protocolVersion, @NonNull String serverAddress,
                                  int serverPort, @NonNull ProtocolState nextState) implements HandshakePacket {}