package net.hypejet.jet.protocol.packet.serverbound.login;

import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;

/**
 * Represents a {@link ServerBoundPacket server-bound packet}, which is sent when a client successfully logs
 * into the server,
 *
 * @since 1.0
 * @author Codestech
 * @see ServerBoundPacket
 */
public interface ClientLoginAcknowledgePacket extends ServerBoundPacket {}