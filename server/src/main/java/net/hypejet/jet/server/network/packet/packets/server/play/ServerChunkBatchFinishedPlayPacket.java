package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.JetChunk;

/**
 * Represents {@linkplain ServerPacket a server packet}, which is sent when a batch
 * of {@linkplain JetChunk chunks} has been fully sent.
 *
 * @param batchSize an amount of chunks that were sent
 * @since 1.0
 * @see JetChunk
 * @see ServerPacket
 */
public record ServerChunkBatchFinishedPlayPacket(int batchSize) implements ServerPacket {}