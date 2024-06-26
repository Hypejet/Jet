package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponseLoginPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientPluginMessageResponseLoginPacket plugin message response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessageResponseLoginPacket
 * @see PacketCodec
 */
public final class ClientPluginMessageResponseLoginPacketCodec extends PacketCodec<ClientPluginMessageResponseLoginPacket> {
    /**
     * Constructs a {@linkplain ClientPluginMessageResponseLoginPacketCodec plugin message response packet codec}.
     *
     * @since 1.0
     */
    public ClientPluginMessageResponseLoginPacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_PLUGIN_MESSAGE_RESPONSE, ClientPluginMessageResponseLoginPacket.class);
    }

    @Override
    public @NonNull ClientPluginMessageResponseLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessageResponseLoginPacket(
                NetworkUtil.readVarInt(buf),
                buf.readBoolean(),
                NetworkUtil.readRemainingBytes(buf)
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPluginMessageResponseLoginPacket object) {
        NetworkUtil.writeVarInt(buf, object.messageId());
        buf.writeBoolean(object.successful());
        buf.writeBytes(object.data());
    }
}