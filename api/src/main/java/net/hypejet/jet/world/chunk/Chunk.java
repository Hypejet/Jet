package net.hypejet.jet.world.chunk;

import net.hypejet.jet.protocol.packet.server.play.ServerChunkAndLightDataPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents a 16x16 part of a {@linkplain net.hypejet.jet.world.World world}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.world.World
 */
public interface Chunk {
    /**
     * Gets sections of the chunk.
     *
     * @return the sections
     * @since 1.0
     */
    @NonNull Collection<ChunkSection> sections();

    /**
     * Gets a section at a specified height of the chunk.
     *
     * @param height the height
     * @return the section, {@code null} if the chunk does not contain a chunk section at a specified height
     * @since 1.0
     */
    @NonNull ChunkSection sectionAt(int height);

    /**
     * Creates a {@linkplain ServerChunkAndLightDataPlayPacket chunk and light data play packet}.
     *
     * @param chunkX a position of the chunk at an {@code X} axis
     * @param chunkZ a position of the chunk at a {@code Z} axis
     * @return the packet
     * @since 1.0
     */
    @NonNull ServerChunkAndLightDataPlayPacket createChunkAndLightDataPacket(int chunkX, int chunkZ);
}