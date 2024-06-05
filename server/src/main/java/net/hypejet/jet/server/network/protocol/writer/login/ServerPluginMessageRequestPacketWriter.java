package net.hypejet.jet.server.network.protocol.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestPacket;
import net.hypejet.jet.server.network.protocol.writer.PacketWriter;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketWriter packet writer}, which reads and writes
 * a {@linkplain ServerPluginMessageRequestPacket plugin message request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestPacket
 * @see PacketWriter
 */
public final class ServerPluginMessageRequestPacketWriter extends PacketWriter<ServerPluginMessageRequestPacket> {
    /**
     * Constructs the {@linkplain ServerPluginMessageRequestPacketWriter plugin message request packet writer}.
     *
     * @since 1.0
     */
    public ServerPluginMessageRequestPacketWriter() {
        super(4, ServerPluginMessageRequestPacket.class);
    }

    @Override
    public @NonNull ServerPluginMessageRequestPacket read(@NonNull ByteBuf buf) {
        return new ServerPluginMessageRequestPacket(
                NetworkUtil.readVarInt(buf),
                NetworkUtil.readIdentifier(buf),
                NetworkUtil.readRemainingBytes(buf)
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessageRequestPacket object) {
        NetworkUtil.writeVarInt(buf, object.messageId());
        NetworkUtil.writeIdentifier(buf, object.channel());
        buf.writeBytes(object.data());
    }
}
