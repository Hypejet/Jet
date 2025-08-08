package net.hypejet.jet.server.network.codec.packet.server.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.KeyNetworkCodec;
import net.hypejet.jet.server.network.codec.game.registry.RegistryDataEntryNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.configuration.ServerRegistryDataConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerRegistryDataConfigurationPacket a registry data configuration packet}.
 *
 * @since 1.0
 * @see ServerRegistryDataConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerRegistryDataConfigurationPacketWriter
        implements NetworkWriter<ServerRegistryDataConfigurationPacket> {

    /**
     * An instance of the {@linkplain ServerRegistryDataConfigurationPacketWriter server registry data configuration
     * packet writer}.
     *
     * @since 1.0
     */
    public static final ServerRegistryDataConfigurationPacketWriter
            INSTANCE = new ServerRegistryDataConfigurationPacketWriter();

    private ServerRegistryDataConfigurationPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerRegistryDataConfigurationPacket object) {
        KeyNetworkCodec.INSTANCE.write(buf, object.registry());
        RegistryDataEntryNetworkWriter.COLLECTION_WRITER.write(buf, object.entries());
    }
}