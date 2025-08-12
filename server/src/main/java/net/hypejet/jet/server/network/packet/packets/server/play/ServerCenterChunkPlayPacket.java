package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.JetChunk;
import net.hypejet.jet.world.coordinate.chunk.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ServerPacket a server packet}, which sets {@linkplain ChunkPosition a chunk position}
 * of a center {@linkplain JetChunk chunk} of a client chunk loading area.
 *
 * @param chunkPosition the center chunk position
 * @since 1.0
 * @see ChunkPosition
 * @see JetChunk
 */
public record ServerCenterChunkPlayPacket(@NonNull ChunkPosition chunkPosition) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerCenterChunkPlayPacket server center chunk play packet}.
     *
     * @param chunkPosition the center chunk position
     * @since 1.0
     */
    public ServerCenterChunkPlayPacket {
        Objects.requireNonNull(chunkPosition, "chunk position");
    }
}