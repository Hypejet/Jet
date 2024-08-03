package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when player changes their position
 * on client.
 *
 * @param x an absolute {@code X} value of the position
 * @param feetY an absolute feet position of the player, normally head {@code Y} - {@code 1.62}
 * @param z an absolute {@code Z} value of the position
 * @param onGround whether the player is on ground on client
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientPositionPlayPacket(double x, double feetY, double z, boolean onGround)
        implements ClientPlayPacket {}