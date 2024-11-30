package net.hypejet.jet.network.packet.server.configuration;

import net.hypejet.jet.network.packet.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which requests client to finish the configuration state.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.network.ProtocolState#CONFIGURATION
 * @see ServerPacket
 */
public record ServerFinishConfigurationPacket() implements ServerPacket {}