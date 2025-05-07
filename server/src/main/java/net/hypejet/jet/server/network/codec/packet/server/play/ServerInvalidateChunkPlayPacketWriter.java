package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.chunk.ChunkPositionNetworkWriter;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerInvalidateChunkPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerInvalidateChunkPlayPacket a server invalidate chunk play packet}.
 *
 * @since .10
 * @see ServerInvalidateChunkPlayPacket
 * @see NetworkWriter
 */
public final class ServerInvalidateChunkPlayPacketWriter implements NetworkWriter<ServerInvalidateChunkPlayPacket> {
    /**
     * An instance of the {@linkplain ServerInvalidateChunkPlayPacketWriter server invalidate chunk play packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerInvalidateChunkPlayPacketWriter INSTANCE = new ServerInvalidateChunkPlayPacketWriter();

    private ServerInvalidateChunkPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerInvalidateChunkPlayPacket object) {
        ChunkPositionNetworkWriter.INSTANCE.write(buf, object.chunkPosition());
    }
}