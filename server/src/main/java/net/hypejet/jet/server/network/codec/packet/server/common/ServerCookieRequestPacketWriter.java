package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCookieRequestPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerCookieRequestPacket a server
 * cookie request packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestPacket
 * @see NetworkWriter
 */
public final class ServerCookieRequestPacketWriter implements NetworkWriter<ServerCookieRequestPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCookieRequestPacket object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.key());
    }
}