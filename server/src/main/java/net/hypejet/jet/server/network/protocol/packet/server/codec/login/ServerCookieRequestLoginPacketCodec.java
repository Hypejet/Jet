package net.hypejet.jet.server.network.protocol.packet.server.codec.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestLoginPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerCookieRequestLoginPacket cookie request login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestLoginPacket
 * @see PacketCodec
 */
public final class ServerCookieRequestLoginPacketCodec extends PacketCodec<ServerCookieRequestLoginPacket> {
    /**
     * Constructs the {@linkplain ServerCookieRequestLoginPacketCodec cookie request packet codec}.
     *
     * @since 1.0
     */
    public ServerCookieRequestLoginPacketCodec() {
        super(ServerPacketIdentifiers.LOGIN_COOKIE_REQUEST, ServerCookieRequestLoginPacket.class);
    }

    @Override
    public @NonNull ServerCookieRequestLoginPacket read(@NonNull ByteBuf buf) {
        return new ServerCookieRequestLoginPacket(NetworkUtil.readIdentifier(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCookieRequestLoginPacket object) {
        NetworkUtil.writeIdentifier(buf, object.identifier());
    }
}