package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client acknowledges
 * chat messages.
 *
 * @param messageCount a count of the chat messages, which were acknowledged
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientAcknowledgeMessagePacket(int messageCount) implements ClientPlayPacket {}