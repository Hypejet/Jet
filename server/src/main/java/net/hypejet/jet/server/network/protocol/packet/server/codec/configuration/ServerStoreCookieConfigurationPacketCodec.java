package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerStoreCookieConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.identifier.PackedIdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import net.hypejet.jet.server.util.NetworkUtil;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerStoreCookieConfigurationPacket store cookie configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerStoreCookieConfigurationPacket
 * @see PacketCodec
 */
public final class ServerStoreCookieConfigurationPacketCodec
        extends PacketCodec<ServerStoreCookieConfigurationPacket> {

    private static final int MAX_COOKIE_LENGTH = 5120;

    /**
     * Constructs the {@linkplain ServerStoreCookieConfigurationPacketCodec store cookie configuration packet codec}.
     *
     * @since 1.0
     */
    public ServerStoreCookieConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_STORE_COOKIE, ServerStoreCookieConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerStoreCookieConfigurationPacket read(@NonNull ByteBuf buf) {
        Key identifier = PackedIdentifierNetworkCodec.instance().read(buf);
        if (buf.readableBytes() > MAX_COOKIE_LENGTH) throw invalidCookieLengthException();
        return new ServerStoreCookieConfigurationPacket(identifier, NetworkUtil.readRemainingBytes(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerStoreCookieConfigurationPacket object) {
        Key identifier = object.identifier();
        byte[] data = object.data();

        if (data.length > MAX_COOKIE_LENGTH) throw invalidCookieLengthException();

        PackedIdentifierNetworkCodec.instance().write(buf, identifier);
        buf.writeBytes(data);
    }

    private static @NonNull IllegalArgumentException invalidCookieLengthException() {
        return new IllegalArgumentException("The max length of a cookie is: " + MAX_COOKIE_LENGTH);
    }
}