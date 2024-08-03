package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which indicates whether a player is on a ground.
 *
 * @param onGround {@code true} if the player is on a ground, {@code false} otherwise
 * @since 1.0
 * @author Codestech
 */
public record ClientOnGroundPlayPacket(boolean onGround) implements ClientPlayPacket {}