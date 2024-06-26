package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * to acknowledge a finish of the configuration.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientConfigurationPacket
 */
public record ClientAcknowledgeFinishConfigurationPacket() implements ClientConfigurationPacket {}