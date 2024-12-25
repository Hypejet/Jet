package net.hypejet.jet.server.network.codec.packet.server.common;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.registry.tag.TagRegistryNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerUpdateTagsPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes {@linkplain ServerUpdateTagsPacket a server
 * update tags packet}.
 *
 * @since 1.0
 * @see ServerUpdateTagsPacket
 * @see NetworkWriter
 */
public final class ServerUpdateTagsPacketWriter implements NetworkWriter<ServerUpdateTagsPacket> {

    /**
     * An instance of the {@linkplain ServerUpdateTagsPacketWriter server update tags packet writer}.
     *
     * @since 1.0
     */
    public static final ServerUpdateTagsPacketWriter INSTANCE = new ServerUpdateTagsPacketWriter();

    private ServerUpdateTagsPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerUpdateTagsPacket object) {
        TagRegistryNetworkWriter.COLLECTION_WRITER.write(buf, object.registries());
    }
}