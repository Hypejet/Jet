package net.hypejet.jet.protocol.packet.client.status;

import net.hypejet.jet.protocol.packet.client.ClientStatusPacket;

/**
 * Represents a {@linkplain ClientStatusPacket client status packet}, which is sent by a client, when it requests
 * a {@linkplain net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket server list response
 * status packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.status.ServerListResponseStatusPacket
 * @see ClientStatusPacket
 */
public record ClientServerListRequestStatusPacket() implements ClientStatusPacket {}