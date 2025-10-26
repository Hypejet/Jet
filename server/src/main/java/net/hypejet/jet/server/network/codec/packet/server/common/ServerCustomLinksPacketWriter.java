package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.link.ServerLinkNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerCustomLinksPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerCustomLinksPacket a server
 * custom links packet}.
 *
 * @since 1.0
 * @see ServerCustomLinksPacket
 * @see NetworkWriter
 */
public final class ServerCustomLinksPacketWriter implements NetworkWriter<ServerCustomLinksPacket> {
    /**
     * An instance of the {@linkplain ServerCustomLinksPacketWriter server custom links packet writer}.
     *
     * @since 1.0
     */
    public static final ServerCustomLinksPacketWriter INSTANCE = new ServerCustomLinksPacketWriter();

    private ServerCustomLinksPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ServerCustomLinksPacket object) {
        ServerLinkNetworkWriter.COLLECTION_WRITER.write(buf, registryManager, object.serverLinks());
    }
}