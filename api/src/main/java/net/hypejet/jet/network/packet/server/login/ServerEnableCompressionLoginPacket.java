package net.hypejet.jet.network.packet.server.login;

import net.hypejet.jet.network.packet.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet} enabling a compression in the network.
 *
 * @param compressionThreshold a minimum size of a packet to be compressed
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public record ServerEnableCompressionLoginPacket(int compressionThreshold) implements ServerPacket {}