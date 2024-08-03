package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import net.hypejet.jet.signing.SeenMessages;
import net.hypejet.jet.signing.SignedArgument;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client sends a chat command
 * with signed arguments.
 *
 * @param command the command
 * @param timestamp a timestamp that the command was executed at
 * @param salt a salt of the following signed arguments
 * @param arguments arguments of the command and their signatures
 * @param seenMessages last messages, which were seen by a client
 * @since 1.0
 */
public record ClientSignedChatCommandPlayPacket(@NonNull String command, long timestamp, long salt,
                                                @NonNull Collection<SignedArgument> arguments,
                                                @NonNull SeenMessages seenMessages) implements ClientPlayPacket {
    /**
     * Constructs the {@linkplain ClientSignedChatCommandPlayPacket signed chat command play packet}.
     *
     * @param command the command
     * @param timestamp a timestamp that the command was executed at
     * @param salt a salt of the following signed arguments
     * @param arguments arguments of the command and their signatures
     * @param seenMessages last messages, which were seen by a client
     * @since 1.0
     */
    public ClientSignedChatCommandPlayPacket {
        arguments = Set.copyOf(arguments);
    }
}