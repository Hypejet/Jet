package net.hypejet.jet.protocol.packet.server.play;

import net.hypejet.jet.protocol.packet.server.ServerPlayPacket;

/**
 * Represents a {@linkplain ServerPlayPacket server play packet}, which is sent to allow server
 * to check if a connection with client is working properly.
 *
 * @param keepAliveIdentifier an identifier of the keep-alive
 * @since 1.0
 * @author Codestech
 * @see ServerPlayPacket
 */
public record ServerKeepAlivePlayPacket(long keepAliveIdentifier) implements ServerPlayPacket {}