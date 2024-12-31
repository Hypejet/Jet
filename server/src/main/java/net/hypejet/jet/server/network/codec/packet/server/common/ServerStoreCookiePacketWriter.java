package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerStoreCookiePacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerStoreCookiePacket a server
 * store cookie packet}.
 *
 * @since 1.0
 * @see ServerStoreCookiePacket
 * @see NetworkWriter
 */
public final class ServerStoreCookiePacketWriter implements NetworkWriter<ServerStoreCookiePacket> {

    /**
     * An instance of the {@linkplain ServerStoreCookiePacketWriter sever store cookie packet writer}.
     *
     * @since 1.0
     */
    public static final ServerStoreCookiePacketWriter INSTANCE = new ServerStoreCookiePacketWriter();

    private static final int MAX_COOKIE_LENGTH = 5120;

    private ServerStoreCookiePacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerStoreCookiePacket object) {
        byte[] data = object.data().array();
        if (data.length > MAX_COOKIE_LENGTH)
            throw new IllegalArgumentException(String.format("The max length of a cookie is %s", MAX_COOKIE_LENGTH));

        PackedKeyNetworkCodec.INSTANCE.write(buf, object.key());
        buf.writeBytes(data);
    }
}