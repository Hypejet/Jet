package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPingResponsePacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerPingResponsePacket a ping
 * response packet}.
 *
 * @since 1.0
 * @see ServerPingResponsePacket
 * @see NetworkWriter
 */
public final class ServerPingResponsePacketWriter implements NetworkWriter<ServerPingResponsePacket> {
    /**
     * An instance of the {@linkplain ServerPingResponsePacketWriter server ping response packet writer}.
     *
     * @since 1.0
     */
    public static final ServerPingResponsePacketWriter INSTANCE = new ServerPingResponsePacketWriter();

    private ServerPingResponsePacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ServerPingResponsePacket object) {
        buf.writeLong(object.payload());
    }
}