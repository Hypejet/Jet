package net.hypejet.jet.network.packet.client.login;

import net.hypejet.jet.network.packet.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which accepts finish of the login protocol state.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.network.ProtocolState#LOGIN
 * @see ClientPacket
 */
public record ClientLoginAcknowledgeLoginPacket() implements ClientPacket {}