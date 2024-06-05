package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponsePacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientCookieResponsePacket cookie response packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponsePacket
 * @see PacketCodec
 */
public final class ClientCookieResponsePacketCodec extends PacketCodec<ClientCookieResponsePacket> {
    /**
     * Constructs the {@linkplain ClientCookieResponsePacketCodec cookie response packet codec}.
     *
     * @since 1.0
     */
    public ClientCookieResponsePacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_COOKIE_RESPONSE, ClientCookieResponsePacket.class);
    }

    @Override
    public @NonNull ClientCookieResponsePacket read(@NonNull ByteBuf buf) {
        return new ClientCookieResponsePacket(
                NetworkUtil.readIdentifier(buf),
                buf.readBoolean() ? NetworkUtil.readByteArray(buf) : null
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientCookieResponsePacket object) {
        NetworkUtil.writeIdentifier(buf, object.identifier());

        byte[] data = object.data();
        buf.writeBoolean(data != null);

        if (data != null) {
            NetworkUtil.writeByteArray(buf, data);
        }
    }
}
