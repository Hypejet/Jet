package net.hypejet.jet.server.network.packet.packets.client.common;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client when it wants to calculate a ping.
 *
 * @param timestamp a system-dependent timestamp value counted in milliseconds of the time when the request was made
 * @since 1.0
 * @see ClientPacket
 */
public record ClientPingRequestPacket(long timestamp) implements ClientPacket {}