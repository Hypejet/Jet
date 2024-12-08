package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.signing.SeenMessages;
import net.hypejet.jet.signing.SignedArgument;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Set;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client to invoke a command with signed
 * arguments.
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
                                                @NonNull SeenMessages seenMessages) implements ClientPacket {
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