package net.hypejet.jet.server.network.protocol.packet.client.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.client.login.ClientCookieResponseLoginPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ClientCookieResponseLoginPacket cookie response login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientCookieResponseLoginPacket
 * @see PacketCodec
 */
public final class ClientCookieResponseLoginPacketCodec extends PacketCodec<ClientCookieResponseLoginPacket> {
    /**
     * Constructs the {@linkplain ClientCookieResponseLoginPacketCodec cookie response packet codec}.
     *
     * @since 1.0
     */
    public ClientCookieResponseLoginPacketCodec() {
        super(ClientPacketIdentifiers.LOGIN_COOKIE_RESPONSE, ClientCookieResponseLoginPacket.class);
    }

    @Override
    public @NonNull ClientCookieResponseLoginPacket read(@NonNull ByteBuf buf) {
        return new ClientCookieResponseLoginPacket(
                NetworkUtil.readIdentifier(buf),
                buf.readBoolean() ? NetworkUtil.readByteArray(buf) : null
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ClientCookieResponseLoginPacket object) {
        NetworkUtil.writeIdentifier(buf, object.identifier());

        byte[] data = object.data();
        buf.writeBoolean(data != null);

        if (data != null) {
            NetworkUtil.writeByteArray(buf, data);
        }
    }
}
