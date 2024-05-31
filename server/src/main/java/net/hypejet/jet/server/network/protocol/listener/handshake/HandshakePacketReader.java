package net.hypejet.jet.server.network.protocol.listener.handshake;

import net.hypejet.jet.player.PlayerConnection;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.listener.PacketReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link PacketReader packet reader}, which reads a {@link ClientHandshakePacket handshake packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientHandshakePacket
 */
public final class HandshakePacketReader extends PacketReader<ClientHandshakePacket> {

    /**
     * Constructs a {@link HandshakePacketReader handshake packet reader}.
     *
     * @since 1.0
     */
    public HandshakePacketReader() {
        super(0, ProtocolState.HANDSHAKE);
    }

    @Override
    public @NonNull ClientHandshakePacket read(@NonNull NetworkBuffer buffer) {
        int protocolVersion = buffer.readVarInt();
        String serverAddress = buffer.readString();
        int serverPort = buffer.readUnsignedShort();
        int nextStateId = buffer.readVarInt();

        ProtocolState nextState = switch (nextStateId) {
            case 1 -> ProtocolState.STATUS;
            case 2 -> ProtocolState.LOGIN;
            default -> throw new IllegalStateException("Invalid protocol state: " + nextStateId);
        };

        return new ClientHandshakePacketImpl(protocolVersion, serverAddress, serverPort, nextState);
    }

    /**
     * Represents an implementation of {@link ClientHandshakePacket handshake packet}.
     *
     * @param protocolVersion a version of the Minecraft protocol
     * @param serverAddress an address of server that a client is trying to connect to
     * @param serverPort a port of server that a client is trying to connect to
     * @param nextState a next {@link ProtocolState protocol state}, which a {@link PlayerConnection player connection}
     *                  will switch to.
     */
    private record ClientHandshakePacketImpl(int protocolVersion, @NonNull String serverAddress,
                                             int serverPort, @NonNull ProtocolState nextState) implements ClientHandshakePacket {}
}