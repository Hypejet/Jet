package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet} requesting preserving a connection between a client and
 * a server.
 *
 * @param keepAliveIdentifier an identifier of the request, client responds with the same key
 * @since 1.0
 * @see ServerPacket
 */
public record ServerKeepAlivePacket(long keepAliveIdentifier) implements ServerPacket {}