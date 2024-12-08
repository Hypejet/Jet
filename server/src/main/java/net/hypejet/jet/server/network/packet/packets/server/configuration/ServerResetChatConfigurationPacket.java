package net.hypejet.jet.server.network.packet.packets.server.configuration;

import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet} clearing chat history of the client.
 *
 * @since 1.0
 * @author Codestech
 * @see ProtocolState#CONFIGURATION
 * @see ServerPacket
 */
public record ServerResetChatConfigurationPacket() implements ServerPacket {}