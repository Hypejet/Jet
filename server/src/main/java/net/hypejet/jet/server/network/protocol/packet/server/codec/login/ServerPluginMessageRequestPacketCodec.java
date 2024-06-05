package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerPluginMessageRequestPacket;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.network.protocol.packet.server.codec.ServerPacketCodec;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerPacketCodec server packet codec}, which reads and writes
 * a {@linkplain ServerPluginMessageRequestPacket plugin message request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPluginMessageRequestPacket
 * @see ServerPacketCodec
 */
public final class ServerPluginMessageRequestPacketCodec extends ServerPacketCodec<ServerPluginMessageRequestPacket> {
    /**
     * Constructs the {@linkplain ServerPluginMessageRequestPacketCodec plugin message request packet codec}.
     *
     * @since 1.0
     */
    public ServerPluginMessageRequestPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_PLUGIN_MESSAGE_REQUEST, ServerPluginMessageRequestPacket.class);
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
