package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerKeepAlivePacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerKeepAlivePacket a server
 * keep alive packet}.
 *
 * @since 1.0
 * @see ServerKeepAlivePacket
 * @see NetworkWriter
 */
public final class ServerKeepAlivePacketWriter implements NetworkWriter<ServerKeepAlivePacket> {
    /**
     * An instance of the {@linkplain ServerKeepAlivePacketWriter server keep alive packet writer}.
     *
     * @since 1.0
     */
    public static final ServerKeepAlivePacketWriter INSTANCE = new ServerKeepAlivePacketWriter();

    private ServerKeepAlivePacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ServerKeepAlivePacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }
}