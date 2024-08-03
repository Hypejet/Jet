package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client invokes a chat
 * command.
 *
 * @param commandString the unparsed chat command
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientChatCommandPlayPacket(@NonNull String commandString) implements ClientPlayPacket {}