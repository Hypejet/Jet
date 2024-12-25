package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.server.network.packet.packets.client.common.ClientPongPacket;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which is used to request
 * {@linkplain ClientPongPacket a client pong packet} from a client.
 *
 * @param identifier an identifier of the ping
 * @since 1.0
 * @see ClientPongPacket
 * @see ServerPacket
 */
public record ServerPingPacket(int identifier) implements ServerPacket {}