package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which sets the center chunk position of a chunk
 * loading area of a client.
 *
 * @param chunkX an {@code X} value of the position
 * @param chunkZ an {@code Z} value of the position
 * @since 1.0
 * @author Codestech
 */
public record ServerCenterChunkPlayPacket(int chunkX, int chunkZ) implements ServerPlayPacket {}