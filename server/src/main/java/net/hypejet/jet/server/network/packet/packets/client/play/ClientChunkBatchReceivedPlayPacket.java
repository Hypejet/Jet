package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is received when a client received a batch
 * of {@linkplain net.hypejet.jet.server.world.chunk.Chunk chunks}.
 *
 * @param desiredChunksPerTick a number of chunks that the client wants to be sent per tick
 * @since 1.0
 */
public record ClientChunkBatchReceivedPlayPacket(float desiredChunksPerTick) implements ClientPacket {}