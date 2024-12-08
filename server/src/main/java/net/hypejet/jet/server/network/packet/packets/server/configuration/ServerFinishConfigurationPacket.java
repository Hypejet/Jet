package net.hypejet.jet.server.network.packet.packets.server.configuration;

import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet}, which requests client to finish the configuration state.
 *
 * @since 1.0
 * @author Codestech
 * @see ProtocolState#CONFIGURATION
 * @see ServerPacket
 */
public record ServerFinishConfigurationPacket() implements ServerPacket {}