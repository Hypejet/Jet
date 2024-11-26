package net.hypejet.jet.server.network.protocol.packet.server.writer.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.login.ServerCookieRequestLoginPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.game.key.PackedKeyNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCookieRequestLoginPacket a cookie request login packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestLoginPacket
 * @see NetworkWriter
 */
public final class ServerCookieRequestLoginPacketWriter implements NetworkWriter<ServerCookieRequestLoginPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCookieRequestLoginPacket object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.identifier());
    }
}