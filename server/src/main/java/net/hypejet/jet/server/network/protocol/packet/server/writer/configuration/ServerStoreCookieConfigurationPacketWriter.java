package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerStoreCookieConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.key.PackedKeyNetworkCodec;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerStoreCookieConfigurationPacket a store cookie configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerStoreCookieConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerStoreCookieConfigurationPacketWriter
        implements NetworkWriter<ServerStoreCookieConfigurationPacket> {

    private static final int MAX_COOKIE_LENGTH = 5120;

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerStoreCookieConfigurationPacket object) {
        Key identifier = object.identifier();
        byte[] data = object.data();

        if (data.length > MAX_COOKIE_LENGTH)
            throw new IllegalArgumentException(String.format("The max length of a cookie is %s", MAX_COOKIE_LENGTH));

        PackedKeyNetworkCodec.INSTANCE.write(buf, identifier);
        buf.writeBytes(data);
    }
}