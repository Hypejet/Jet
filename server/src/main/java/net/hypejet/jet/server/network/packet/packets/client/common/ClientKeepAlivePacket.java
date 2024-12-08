package net.hypejet.jet.server.network.packet.packets.client.common;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is a response to a connection preserve request
 * sent by a server.
 *
 * @param keepAliveIdentifier a numeric identifier of the keep alive
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public record ClientKeepAlivePacket(long keepAliveIdentifier) implements ClientPacket {}