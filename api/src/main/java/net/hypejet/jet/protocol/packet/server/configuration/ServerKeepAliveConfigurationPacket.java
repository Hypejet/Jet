package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which is sent to allow server
 * to check if a connection with client is working properly.
 *
 * @param keepAliveIdentifier an identifier of the keep-alive
 * @since 1.0
 * @author Codestech
 * @see ServerConfigurationPacket
 */
public record ServerKeepAliveConfigurationPacket(int keepAliveIdentifier) implements ServerConfigurationPacket {}