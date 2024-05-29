package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientPacket;

/**
 * Represents a {@linkplain ClientPacket server-bound packet}, which is sent when a client successfully logs into the
 * server.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public interface ClientLoginAcknowledgePacket extends ClientPacket {}