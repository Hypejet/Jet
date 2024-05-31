package net.hypejet.jet.server.network.protocol.writer.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.compression.ServerEnableCompressionPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ServerEnableCompressionPacketWriter extends PacketWriter<ServerEnableCompressionPacket> {

    public ServerEnableCompressionPacketWriter() {
        super(3, ProtocolState.LOGIN, ServerEnableCompressionPacket.class);
    }

    @Override
    public void write(@NonNull ServerEnableCompressionPacket packet, @NonNull NetworkBuffer buffer) {
        buffer.writeVarInt(packet.threshold());
    }
}
