package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes a {@linkplain ServerCookieRequestPacket
 * cookie request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestPacket
 * @see PacketCodec
 */
public final class ServerCookieRequestPacketCodec extends PacketCodec<ServerCookieRequestPacket> {
    /**
     * Constructs the {@linkplain ServerCookieRequestPacketCodec cookie request packet codec}.
     *
     * @since 1.0
     */
    public ServerCookieRequestPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_COOKIE_REQUEST, ServerCookieRequestPacket.class);
    }

    @Override
    public @NonNull ServerCookieRequestPacket read(@NonNull ByteBuf buf) {
        return new ServerCookieRequestPacket(NetworkUtil.readIdentifier(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCookieRequestPacket object) {
        NetworkUtil.writeIdentifier(buf, object.identifier());
    }
}
