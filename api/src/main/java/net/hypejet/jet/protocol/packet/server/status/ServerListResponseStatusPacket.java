package net.hypejet.jet.protocol.packet.server.status;

import net.hypejet.jet.ping.ServerListPing;
import net.hypejet.jet.protocol.packet.server.ServerStatusPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerStatusPacket server status packet}, which contains a server list response data
 * requested by a client by sending
 * a {@linkplain net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket server list
 * request status packet}.
 *
 * @param ping the response data
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.client.status.ClientServerListRequestStatusPacket
 * @see ServerStatusPacket
 */
public record ServerListResponseStatusPacket(@NonNull ServerListPing ping) implements ServerStatusPacket {}