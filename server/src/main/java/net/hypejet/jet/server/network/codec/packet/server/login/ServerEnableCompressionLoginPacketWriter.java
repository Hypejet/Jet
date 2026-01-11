package net.hypejet.jet.server.network.codec.packet.server.login;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerEnableCompressionLoginPacket;
import net.hypejet.jet.server.registry.JetRegistryManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerEnableCompressionLoginPacket an enable compression login packet}.
 *
 * @since 1.0
 * @see ServerEnableCompressionLoginPacket
 * @see NetworkWriter
 */
public final class ServerEnableCompressionLoginPacketWriter
        implements NetworkWriter<ServerEnableCompressionLoginPacket> {
    /**
     * An instance of the {@linkplain ServerEnableCompressionLoginPacketWriter server enable compression login packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerEnableCompressionLoginPacketWriter
            INSTANCE = new ServerEnableCompressionLoginPacketWriter();

    private ServerEnableCompressionLoginPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull JetRegistryManager registryManager,
                      @NonNull ServerEnableCompressionLoginPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, registryManager, object.compressionThreshold());
    }
}