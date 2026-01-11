package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.StringNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerTransferPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerTransferPacket a server
 * transfer packet}.
 *
 * @since 1.0
 * @see ServerTransferPacket
 * @see NetworkWriter
 */
public final class ServerTransferPacketWriter implements NetworkWriter<ServerTransferPacket> {
    /**
     * An instance of the {@linkplain ServerTransferPacketWriter server transfer packet writer}.
     *
     * @since 1.0
     */
    public static final ServerTransferPacketWriter INSTANCE = new ServerTransferPacketWriter();

    private ServerTransferPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf,
                      @NonNull JetRegistryManager registryManager,
                      @NonNull ServerTransferPacket object) {
        StringNetworkCodec.INSTANCE.write(buf, registryManager, object.address());
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.port());
    }
}