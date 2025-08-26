package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * Represents a server packet that triggers an animation on a client.
 *
 * @param entityId the entity ID that should perform the animation
 * @param animation the ID of the animation to play
 * @since 1.0
 */
public record ServerEntityAnimationPlayPacket(int entityId, int animation) implements ServerPacket {
}
