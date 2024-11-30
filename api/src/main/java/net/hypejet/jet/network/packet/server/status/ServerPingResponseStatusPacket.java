package net.hypejet.jet.network.packet.server.status;

import net.hypejet.jet.network.packet.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which responds to a ping requested by a client.
 *
 * @param payload a number, which should be the same as the number sent in the request status packet
 * @since 1.0
 * @author Codestech
 */
public record ServerPingResponseStatusPacket(long payload) implements ServerPacket {}