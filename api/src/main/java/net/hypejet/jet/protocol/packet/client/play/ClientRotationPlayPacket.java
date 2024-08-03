package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when player changes their rotation
 * on client.
 *
 * @param yaw an absolute rotation on the {@code X} axis, in degrees
 * @param pitch an absolute rotation on the {@code Y} axis, in degrees
 * @param onGround whether the player is on ground on client
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientRotationPlayPacket(float yaw, float pitch, boolean onGround) implements ClientPlayPacket {}