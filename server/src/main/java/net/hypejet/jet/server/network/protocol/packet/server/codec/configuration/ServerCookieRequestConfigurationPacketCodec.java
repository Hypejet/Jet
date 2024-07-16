package net.hypejet.jet.server.network.protocol.packet.server.codec.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCookieRequestConfigurationPacket;
import net.hypejet.jet.server.network.protocol.codecs.other.IdentifierNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerCookieRequestConfigurationPacket cookie request configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCookieRequestConfigurationPacket
 * @see PacketCodec
 */
public final class ServerCookieRequestConfigurationPacketCodec
        extends PacketCodec<ServerCookieRequestConfigurationPacket> {
    /**
     * Constructs the {@linkplain ServerCookieRequestConfigurationPacketCodec cookie request configuration packet
     * codec}.
     *
     * @since 1.0
     */
    public ServerCookieRequestConfigurationPacketCodec() {
        super(ServerPacketIdentifiers.CONFIGURATION_COOKIE_REQUEST, ServerCookieRequestConfigurationPacket.class);
    }

    @Override
    public @NonNull ServerCookieRequestConfigurationPacket read(@NonNull ByteBuf buf) {
        return new ServerCookieRequestConfigurationPacket(IdentifierNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCookieRequestConfigurationPacket object) {
        IdentifierNetworkCodec.instance().write(buf, object.identifier());
    }
}