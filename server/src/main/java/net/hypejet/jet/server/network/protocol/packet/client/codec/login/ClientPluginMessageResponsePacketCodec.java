package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientPluginMessageResponsePacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientPluginMessageResponsePacket plugin message response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPluginMessageResponsePacket
 * @see PacketCodec
 */
public final class ClientPluginMessageResponsePacketCodec extends PacketCodec<ClientPluginMessageResponsePacket> {
    /**
     * Constructs a {@linkplain ClientPluginMessageResponsePacketCodec plugin message response packet codec}.
     *
     * @since 1.0
     */
    public ClientPluginMessageResponsePacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_PLUGIN_MESSAGE_RESPONSE, ClientPluginMessageResponsePacket.class);
    }

    @Override
    public @NonNull ClientPluginMessageResponsePacket read(@NonNull ByteBuf buf) {
        return new ClientPluginMessageResponsePacket(
                NetworkUtil.readVarInt(buf),
                buf.readBoolean(),
                NetworkUtil.readRemainingBytes(buf)
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientPluginMessageResponsePacket object) {
        NetworkUtil.writeVarInt(buf, object.messageId());
        buf.writeBoolean(object.successful());
        buf.writeBytes(object.data());
    }
}