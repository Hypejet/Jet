package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which is sent when a batch
 * of {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunks} has been fully sent.
 *
 * @param batchSize an amount of chunks that were sent
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 * @see ServerPacket
 */
public record ServerChunkBatchFinishedPlayPacket(int batchSize) implements ServerPacket {}