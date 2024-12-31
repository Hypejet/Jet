package net.hypejet.jet.server.network.packet.packets.client.login;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which accepts finish of the login protocol state.
 *
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#LOGIN
 * @see ClientPacket
 */
public record ClientLoginAcknowledgeLoginPacket() implements ClientPacket {}