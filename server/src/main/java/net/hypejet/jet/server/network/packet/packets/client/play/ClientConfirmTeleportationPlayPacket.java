package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which confirms a teleportation on a client.
 *
 * @param teleportationId an identifier of the teleportation
 * @since 1.0
 * @see ClientPacket
 */
public record ClientConfirmTeleportationPlayPacket(int teleportationId) implements ClientPacket {}