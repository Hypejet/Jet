package net.hypejet.jet.server.network.packet.packets.server.configuration;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents {@linkplain ServerPacket a server packet} clearing chat history of the client.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#CONFIGURATION
 * @see ServerPacket
 */
public record ServerResetChatConfigurationPacket() implements ServerPacket {}