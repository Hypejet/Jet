package net.hypejet.jet.server.network.protocol.packet.server.writer.configuration;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.configuration.ServerCustomLinksConfigurationPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.game.link.ServerLinkNetworkWriter;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCustomLinksConfigurationPacket a custom links configuration packet}.
 *
 * @since 1.0
 * @author Codestech@
 * @see ServerCustomLinksConfigurationPacket
 * @see NetworkWriter
 */
public final class ServerCustomLinksConfigurationPacketWriter
        implements NetworkWriter<ServerCustomLinksConfigurationPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCustomLinksConfigurationPacket object) {
        ServerLinkNetworkWriter.COLLECTION_WRITER.write(buf, object.serverLinks());
    }
}