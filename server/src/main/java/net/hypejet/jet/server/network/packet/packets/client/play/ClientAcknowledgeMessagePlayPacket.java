package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which acknowledges that client has received chat messages.
 *
 * @param messageCount a count of the received chat messages
 * @since 1.0
 * @see ClientPacket
 */
public record ClientAcknowledgeMessagePlayPacket(int messageCount) implements ClientPacket {}