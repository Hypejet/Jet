package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.coordinate.ChunkPosition;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which sets {@linkplain ChunkPosition a chunk position}
 * of a center {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunk} of a client chunk loading area.
 *
 * @param chunkPosition the center chunk position
 * @since 1.0
 * @see ChunkPosition
 * @see net.hypejet.jet.server.world.chunk.Chunk
 */
public record ServerCenterChunkPlayPacket(@NonNull ChunkPosition chunkPosition) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerCenterChunkPlayPacket server center chunk play packet}.
     *
     * @param chunkPosition the center chunk position
     * @since 1.0
     */
    public ServerCenterChunkPlayPacket {
        NullabilityUtil.requireNonNull(chunkPosition, "chunk position");
    }
}