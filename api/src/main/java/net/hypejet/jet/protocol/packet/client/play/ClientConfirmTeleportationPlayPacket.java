package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client confirms
 * a teleportation.
 *
 * @param teleportationId an identifier of the teleportation
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientConfirmTeleportationPlayPacket(int teleportationId) implements ClientPlayPacket {}