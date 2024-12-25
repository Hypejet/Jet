package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which sets the center chunk position of a chunk
 * loading area of a client.
 *
 * @param chunkX an {@code X} value of the position
 * @param chunkZ an {@code Z} value of the position
 * @since 1.0
 */
public record ServerCenterChunkPlayPacket(int chunkX, int chunkZ) implements ServerPacket {}