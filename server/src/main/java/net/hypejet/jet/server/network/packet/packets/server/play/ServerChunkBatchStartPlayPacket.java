package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.server.world.chunk.JetChunk;

/**
 * Represents {@linkplain ServerPacket a server packet}, which informs client that a batch
 * of {@linkplain JetChunk chunks} is going to be sent.
 *
 * @since 1.0
 * @see JetChunk
 * @see ServerPacket
 */
public record ServerChunkBatchStartPlayPacket() implements ServerPacket {}