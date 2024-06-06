package net.hypejet.jet.server.network.protocol.packet.client.codec.handshake;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link PacketCodec paket codec}, which reads and writes
 * a {@link ClientHandshakePacket handshake packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientHandshakePacket
 * @see PacketCodec
 */
public final class HandshakePacketCodec extends PacketCodec<ClientHandshakePacket> {
    /**
     * Constructs the {@linkplain HandshakePacketCodec handshake packet codec}.
     *
     * @since 1.0
     */
    public HandshakePacketCodec() {
        super(ClientPacketIdentifiers.HANDSHAKE, ClientHandshakePacket.class);
    }

    @Override
    public @NonNull ClientHandshakePacket read(@NonNull ByteBuf buf) {
        int protocolVersion = NetworkUtil.readVarInt(buf);
        String serverAddress = NetworkUtil.readString(buf);
        int serverPort = buf.readUnsignedShort();
        int nextStateId = NetworkUtil.readVarInt(buf);

        ProtocolState nextState = switch (nextStateId) {
            case 1 -> ProtocolState.STATUS;
            case 2 -> ProtocolState.LOGIN;
            default -> throw new IllegalStateException("Invalid protocol state: " + nextStateId);
        };

        return new ClientHandshakePacket(protocolVersion, serverAddress, serverPort, nextState);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientHandshakePacket object) {
        NetworkUtil.writeVarInt(buf, object.protocolVersion());
        NetworkUtil.writeString(buf, object.serverAddress());
        buf.writeShort(object.serverPort());

        ProtocolState nextState = object.nextState();
        int nextStateId = switch (nextState) {
            case STATUS -> 1;
            case LOGIN -> 2;
            default -> throw new IllegalStateException("Unexpected protocol state: " + nextState);
        };

        NetworkUtil.writeVarInt(buf, nextStateId);
    }
}