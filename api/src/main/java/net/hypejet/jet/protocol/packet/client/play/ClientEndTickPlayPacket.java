package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which indicates that client has finished a tick.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientEndTickPlayPacket() implements ClientPlayPacket {}