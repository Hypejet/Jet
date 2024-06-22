package net.hypejet.jet.protocol.packet.server.configuration;

import net.hypejet.jet.protocol.packet.server.ServerConfigurationPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerConfigurationPacket server configuration packet}, which changes a server that the
 * client is connected to.
 *
 * @param host a host of the new server
 * @param port a port of the new server
 * @since 1.0
 * @author Codestefch
 * @see net.hypejet.jet.protocol.packet.client.handshake.ClientHandshakePacket.HandshakeIntent#TRANSFER
 * @see ServerConfigurationPacket
 */
public record ServerTransferConfigurationPacket(@NonNull String host, int port)
        implements ServerConfigurationPacket {}