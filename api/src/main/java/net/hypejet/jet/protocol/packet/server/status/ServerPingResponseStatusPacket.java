package net.hypejet.jet.protocol.packet.server.status;

import net.hypejet.jet.protocol.packet.server.ServerStatusPacket;

/**
 * Represents a {@linkplain ServerStatusPacket server status packet}, which is sent as a response for
 * {@linkplain net.hypejet.jet.protocol.packet.client.status.ClientPingRequestStatusPacket ping request status packet}.
 *
 * @param payload a number, which should be the same as the number sent in the ping request status packet
 * @since 1.0
 * @author Codestech
 */
public record ServerPingResponseStatusPacket(int payload) implements ServerStatusPacket {}