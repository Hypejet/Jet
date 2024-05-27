package net.hypejet.jet.server.protocol.packet.serverbound.handshake;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.PacketReader;
import net.hypejet.jet.protocol.packet.serverbound.handshake.HandshakePacket;
import net.hypejet.jet.server.network.PacketDecoder;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class HandshakePacketReader extends PacketReader<HandshakePacket> {

    public HandshakePacketReader() {
        super(0, ProtocolState.HANDSHAKE);
    }

    @Override
    public @NonNull HandshakePacket read(@NonNull ByteBuf buffer) {
        return new HandshakePacket(
                PacketDecoder.readVarInt(buffer),
                PacketDecoder.readString(buffer),
                buffer.readUnsignedShort(),
                PacketDecoder.readVarInt(buffer) == 1
                        ? ProtocolState.STATUS
                        : ProtocolState.LOGIN
        );
    }
}