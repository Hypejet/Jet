package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerChunkBatchFinishedPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerChunkBatchFinishedPlayPacket a server chunk batch finished play packet}.
 *
 * @since 1.0
 * @see ServerChunkBatchFinishedPlayPacket
 * @see NetworkWriter
 */
public final class ServerChunkBatchFinishedPlayPacketWriter
        implements NetworkWriter<ServerChunkBatchFinishedPlayPacket> {
    /**
     * An instance of the {@linkplain ServerChunkBatchFinishedPlayPacketWriter server chunk batch finished play
     * packet writer}.
     *
     * @since 1.0
     */
    public static final ServerChunkBatchFinishedPlayPacketWriter
            INSTANCE = new ServerChunkBatchFinishedPlayPacketWriter();

    private ServerChunkBatchFinishedPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerChunkBatchFinishedPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.batchSize());
    }
}