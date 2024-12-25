package net.hypejet.jet.server.network.packet.packets.client.configuration;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client to accept a configuration protocol
 * state finish request sent by a server.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#CONFIGURATION
 * @see ClientPacket
 */
public record ClientAcknowledgeFinishConfigurationPacket() implements ClientPacket {}