package net.hypejet.jet.server.network.protocol.writer.login;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.login.success.ServerLoginSuccessPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.buffer.codec.codecs.GameProfilePropertiesCodec;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ServerLoginSuccessPacketWriter extends PacketWriter<ServerLoginSuccessPacket> {

    public ServerLoginSuccessPacketWriter() {
        super(2, ProtocolState.LOGIN, ServerLoginSuccessPacket.class);
    }

    @Override
    public void write(@NonNull ServerLoginSuccessPacket packet, @NonNull NetworkBuffer buffer) {
        buffer.writeUniqueId(packet.uniqueId());
        buffer.writeString(packet.username());
        buffer.writeCollection(GameProfilePropertiesCodec.instance(), packet.properties());
        buffer.writeBoolean(packet.strictErrorHandling());
    }
}