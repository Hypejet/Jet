package net.hypejet.jet.protocol.packet.client.configuration;

import net.hypejet.jet.protocol.packet.client.ClientConfigurationPacket;
import net.hypejet.jet.protocol.packet.server.configuration.ServerKeepAliveConfigurationPacket;

/**
 * Represents a {@linkplain ClientConfigurationPacket client configuration packet}, which is sent by a client
 * as a response for a {@linkplain ServerKeepAliveConfigurationPacket keep alive configuration packet}.
 *
 * @param keepAliveIdentifier an identifier of the keep alive
 * @since 1.0
 * @author Codestech
 * @see ServerKeepAliveConfigurationPacket
 * @see ClientConfigurationPacket
 */
public record ClientKeepAliveConfigurationPacket(long keepAliveIdentifier) implements ClientConfigurationPacket {}