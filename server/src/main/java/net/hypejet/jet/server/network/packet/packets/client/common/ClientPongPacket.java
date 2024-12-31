package net.hypejet.jet.server.network.packet.packets.client.common;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.server.network.packet.packets.server.common.ServerPingPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is a response to a ping sent by a server.
 *
 * @param pingIdentifier a numeric identifier of the ping request
 * @since 1.0
 * @see ServerPingPacket
 * @see ClientPacket
 */
public record ClientPongPacket(int pingIdentifier) implements ClientPacket {}