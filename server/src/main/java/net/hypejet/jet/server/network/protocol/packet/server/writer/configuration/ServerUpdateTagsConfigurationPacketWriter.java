package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerUpdateTagsConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.game.registry.tag.TagRegistryNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerUpdateTagsConfigurationPacket a update tags configuration packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerUpdateTagsConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerUpdateTagsConfigurationPacketWriter
        implements NetworkWriter<ServerUpdateTagsConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerUpdateTagsConfigurationPacket object) {
        TagRegistryNetworkWriter.COLLECTION_WRITER.write(buf, object.registries());
    }
}