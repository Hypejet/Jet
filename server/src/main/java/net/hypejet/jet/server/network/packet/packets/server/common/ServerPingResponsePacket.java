package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which responds to a ping requested by a client.
 *
 * @param payload a number, which should be the same number as specified in the ping request
 * @since 1.0
 * @author Codestech
 */
public record ServerPingResponsePacket(long payload) implements ServerPacket {}