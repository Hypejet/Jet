package net.hypejet.jet.network.packet.client.configuration;

import net.hypejet.jet.network.packet.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client to accept
 * {@link net.hypejet.jet.network.ProtocolState#CONFIGURATION a configuration protocol state} finish request
 * sent by a server.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public record ClientAcknowledgeFinishConfigurationPacket() implements ClientPacket {}