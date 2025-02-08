package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which informs client that a batch
 * of {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunks} is going to be sent.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.world.chunk.Chunk
 * @see ServerPacket
 */
public record ServerChunkBatchStartPlayPacket() implements ServerPacket {}