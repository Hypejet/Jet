package net.hypejet.jet.server.network.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.network.packet.server.configuration.ServerRegistryDataConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.key.PackedKeyNetworkCodec;
import net.hypejet.jet.server.network.codec.game.registry.RegistryDataEntryNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerRegistryDataConfigurationPacket a registry data configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerRegistryDataConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerRegistryDataConfigurationPacketWriter
        implements NetworkWriter<ServerRegistryDataConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerRegistryDataConfigurationPacket object) {
        PackedKeyNetworkCodec.INSTANCE.write(buf, object.registry());
        RegistryDataEntryNetworkWriter.COLLECTION_WRITER.write(buf, object.entries());
    }
}