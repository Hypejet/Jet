package net.hypejet.jet.server.network.protocol.packet.server.writer.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerCenterChunkPlayPacket a center chunk play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCenterChunkPlayPacket
 * @see NetworkWriter
 */
public final class ServerCenterChunkPlayPacketWriter implements NetworkWriter<ServerCenterChunkPlayPacket> {
    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCenterChunkPlayPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.chunkX());
        VarIntNetworkCodec.instance().write(buf, object.chunkZ());
    }
}