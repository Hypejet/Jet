package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which is used to request
 * a {@linkplain net.hypejet.jet.protocol.packet.client.configuration.ClientPongConfigurationPacket pong configuration
 * packet} from a client.
 *
 * @param identifier an identifier of the ping
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.client.configuration.ClientPongConfigurationPacket
 * @see ServerConfigurationPacket
 */
public record ServerPingConfigurationPacket(int identifier) implements ServerConfigurationPacket {}
