package net.hypejet.jet.server.network.protocol.writer.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.plugin.ServerPluginMessageRequestPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ServerPluginMessageRequestPacketWriter extends PacketWriter<ServerPluginMessageRequestPacket> {

    public ServerPluginMessageRequestPacketWriter() {
        super(4, ProtocolState.LOGIN, ServerPluginMessageRequestPacket.class);
    }

    @Override
    public void write(@NonNull ServerPluginMessageRequestPacket packet, @NonNull NetworkBuffer buffer) {
        buffer.writeVarInt(packet.messageId());
        buffer.writeIdentifier(packet.channel());
        buffer.writeByteArray(packet.data(), false);
    }
}
