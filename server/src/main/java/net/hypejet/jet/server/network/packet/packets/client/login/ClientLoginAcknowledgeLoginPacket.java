package net.hypejet.jet.server.network.packet.packets.client.login;

import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which accepts finish of
 * the {@linkplain ProtocolState#LOGIN login protocol state}.
 *
 * @since 1.0
 * @author Codestech
 * @see ProtocolState#LOGIN
 * @see ClientPacket
 */
public record ClientLoginAcknowledgeLoginPacket() implements ClientPacket {}