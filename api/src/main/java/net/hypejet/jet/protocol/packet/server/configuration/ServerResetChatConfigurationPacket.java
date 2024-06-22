package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration paket} sent when a server wants to clear
 * chat history saved before the {@linkplain net.hypejet.jet.protocol.ProtocolState#CONFIGURATION configuration
 * protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.ProtocolState#CONFIGURATION
 * @see ServerConfigurationPacket
 */
public record ServerResetChatConfigurationPacket() implements ServerConfigurationPacket {}