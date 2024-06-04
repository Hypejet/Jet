package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet}, which is sent when a client successfully logs into
 * the server.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginPacket
 */
public record ClientLoginAcknowledgePacket() implements ClientLoginPacket {}