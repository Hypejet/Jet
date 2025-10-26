package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPingPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerPingPacket a server ping
 * packet}.
 *
 * @since 1.0
 * @see ServerPingPacket
 * @see NetworkWriter
 */
public final class ServerPingPacketWriter implements NetworkWriter<ServerPingPacket> {
    /**
     * An instance of the {@linkplain ServerPingPacketWriter server ping packet writer}.
     *
     * @since 1.0
     */
    public static final ServerPingPacketWriter INSTANCE = new ServerPingPacketWriter();

    private ServerPingPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ServerPingPacket object) {
        buf.writeInt(object.identifier());
    }
}