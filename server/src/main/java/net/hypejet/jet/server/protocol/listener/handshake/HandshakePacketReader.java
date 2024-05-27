package net.hypejet.jet.server.protocol.listener.handshake;

import net.hypejet.jet.buffer.ReadOnlyNetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.PacketReader;
import net.hypejet.jet.protocol.packet.serverbound.handshake.HandshakePacket;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class HandshakePacketReader extends PacketReader<HandshakePacket> {

    public HandshakePacketReader() {
        super(0, ProtocolState.HANDSHAKE);
    }

    @Override
    public @NonNull HandshakePacket read(@NonNull ReadOnlyNetworkBuffer buffer) {
        return new HandshakePacket(
                buffer.readVarInt(),
                buffer.readString(),
                buffer.readUnsignedShort(),
                buffer.readVarInt() == 1
                        ? ProtocolState.STATUS
                        : ProtocolState.LOGIN
        );
    }
}