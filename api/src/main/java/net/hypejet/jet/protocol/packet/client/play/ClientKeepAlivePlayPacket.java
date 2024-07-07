package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerKeepAlivePlayPacket;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is sent by a client as a response for
 * a {@linkplain ServerKeepAlivePlayPacket keep alive play packet}.
 *
 * @param keepAliveIdentifier an identifier of the keep alive
 * @since 1.0
 * @author Codestech
 * @see ServerKeepAlivePlayPacket
 * @see ClientPlayPacket
 */
public record ClientKeepAlivePlayPacket(long keepAliveIdentifier) implements ClientPlayPacket {}