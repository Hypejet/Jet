package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * as a response for a ping sent by the server via
 * {@linkplain net.hypejet.jet.protocol.packet.server.configuration.ServerPingConfigurationPacket ping configuration
 * packet}.
 *
 * @param pingIdentifier an identifier of the ping that the client responds to
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.configuration.ServerPingConfigurationPacket
 * @see ClientConfigurationPacket
 */
public record ClientPongConfigurationPacket(int pingIdentifier) implements ClientConfigurationPacket {}