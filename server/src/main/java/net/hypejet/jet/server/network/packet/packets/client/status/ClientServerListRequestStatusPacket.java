package net.hypejet.jet.server.network.packet.packets.client.status;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which requests a server list data from a server.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public record ClientServerListRequestStatusPacket() implements ClientPacket {}