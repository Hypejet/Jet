package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestLoginPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec server packet codec}, which reads and writes
 * a {@linkplain ServerPluginMessageRequestLoginPacket plugin message request login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestLoginPacket
 * @see PacketCodec
 */
public final class ServerPluginMessageRequestLoginPacketCodec extends PacketCodec<ServerPluginMessageRequestLoginPacket> {
    /**
     * Constructs the {@linkplain ServerPluginMessageRequestLoginPacketCodec plugin message request packet codec}.
     *
     * @since 1.0
     */
    public ServerPluginMessageRequestLoginPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_PLUGIN_MESSAGE_REQUEST, ServerPluginMessageRequestLoginPacket.class);
    }

    @Override
    public @NonNull ServerPluginMessageRequestLoginPacket read(@NonNull ByteBuf buf) {
        return new ServerPluginMessageRequestLoginPacket(
                NetworkUtil.readVarInt(buf),
                NetworkUtil.readIdentifier(buf),
                NetworkUtil.readRemainingBytes(buf)
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerPluginMessageRequestLoginPacket object) {
        NetworkUtil.writeVarInt(buf, object.messageId());
        NetworkUtil.writeIdentifier(buf, object.channel());
        buf.writeBytes(object.data());
    }
}
