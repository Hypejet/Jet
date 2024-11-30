package net.hypejet.jet.network.packet.server.common;

import net.hypejet.jet.network.packet.client.common.ClientPongPacket;
import net.hypejet.jet.network.packet.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which is used to request
 * {@linkplain ClientPongPacket a client pong packet}.
 * from a client.
 *
 * @param identifier an identifier of the ping
 * @since 1.0
 * @author Codestech
 * @see ClientPongPacket
 * @see ServerPacket
 */
public record ServerPingPacket(int identifier) implements ServerPacket {}