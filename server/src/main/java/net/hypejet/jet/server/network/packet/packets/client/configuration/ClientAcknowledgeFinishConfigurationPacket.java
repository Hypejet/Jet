package net.hypejet.jet.server.network.packet.packets.client.configuration;

import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client to accept
 * {@link ProtocolState#CONFIGURATION a configuration protocol state} finish request
 * sent by a server.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public record ClientAcknowledgeFinishConfigurationPacket() implements ClientPacket {}