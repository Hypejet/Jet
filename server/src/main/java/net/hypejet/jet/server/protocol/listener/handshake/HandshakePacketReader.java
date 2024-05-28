package net.hypejet.jet.server.protocol.listener.handshake;

import net.hypejet.jet.buffer.ReadOnlyNetworkBuffer;
import net.hypejet.jet.player.PlayerConnection;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.server.protocol.PacketReader;
import net.hypejet.jet.protocol.packet.serverbound.handshake.HandshakePacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link PacketReader packet reader}, which reads a {@link HandshakePacket handshake packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see HandshakePacket
 */
public final class HandshakePacketReader extends PacketReader<HandshakePacket> {

    /**
     * Constructs a {@link HandshakePacketReader handshake packet reader}.
     *
     * @since 1.0
     */
    public HandshakePacketReader() {
        super(0, ProtocolState.HANDSHAKE);
    }

    @Override
    public @NonNull HandshakePacket read(@NonNull ReadOnlyNetworkBuffer buffer) {
        return new HandshakePacketImpl(
                buffer.readVarInt(),
                buffer.readString(),
                buffer.readUnsignedShort(),
                buffer.readVarInt() == 1
                        ? ProtocolState.STATUS
                        : ProtocolState.LOGIN
        );
    }

    /**
     * Represents an implementation of {@link HandshakePacket handshake packet}.
     *
     * @param protocolVersion a version of the Minecraft protocol
     * @param serverAddress an address of server that a client is trying to connect to
     * @param serverPort a port of server that a client is trying to connect to
     * @param nextState a next {@link ProtocolState protocol state}, which a {@link PlayerConnection player connection}
     *                  will switch to.
     */
    private record HandshakePacketImpl(int protocolVersion, @NonNull String serverAddress,
                                       int serverPort, @NonNull ProtocolState nextState) implements HandshakePacket {}
}