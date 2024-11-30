package net.hypejet.jet.network.packet.client.play;

import net.hypejet.jet.network.packet.client.ClientPacket;

/**
 * Represents {@linkplain ClientPacket a client packet}, which acknowledges chat messages.
 *
 * @param messageCount a count of the chat messages, which should be acknowledged
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public record ClientAcknowledgeMessagePlayPacket(int messageCount) implements ClientPacket {}