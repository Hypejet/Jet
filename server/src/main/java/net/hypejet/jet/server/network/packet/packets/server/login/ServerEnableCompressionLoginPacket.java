package net.hypejet.jet.server.network.packet.packets.server.login;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet} enabling compression of packets.
 *
 * @param compressionThreshold a minimum size of a packet to be compressed, in bytes
 * @since 1.0
 * @see ServerPacket
 */
public record ServerEnableCompressionLoginPacket(int compressionThreshold) implements ServerPacket {}