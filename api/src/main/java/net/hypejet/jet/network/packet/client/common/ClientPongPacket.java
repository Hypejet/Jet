package net.hypejet.jet.network.packet.client.common;

import net.hypejet.jet.network.packet.client.ClientPacket;
import net.hypejet.jet.network.packet.server.common.ServerPingPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is a response to a ping sent by a server.
 *
 * @param pingIdentifier a numeric identifier of the ping request
 * @since 1.0
 * @author Codestech
 * @see ServerPingPacket
 * @see ClientPacket
 */
public record ClientPongPacket(int pingIdentifier) implements ClientPacket {}