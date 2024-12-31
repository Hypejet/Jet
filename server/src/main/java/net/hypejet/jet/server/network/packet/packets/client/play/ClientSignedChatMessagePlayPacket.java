package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.server.util.game.signing.SeenMessages;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is received when a player on a client sends
 * a signed chat message.
 *
 * @param message the message
 * @param timestamp a timestamp that the chat message was sent at
 * @param salt a salt used to verify the signature hash
 * @param signature the signature, {@code null} if not present
 * @param seenMessages last messages, which were sent by the client
 * @since 1.0
 * @see ClientPacket
 */
public record ClientSignedChatMessagePlayPacket(@NonNull String message, long timestamp,
                                                long salt, @Nullable UnmodifiableByteArray signature,
                                                @NonNull SeenMessages seenMessages) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientSignedChatMessagePlayPacket signed chat message play packet}.
     *
     * @param message the message
     * @param timestamp a timestamp that the chat message was sent at
     * @param salt a salt used to verify the signature hash
     * @param signature the signature, {@code null} if not present
     * @param seenMessages last messages, which were sent by the client
     * @since 1.0
     */
    public ClientSignedChatMessagePlayPacket(@NonNull String message, long timestamp, long salt,
                                             byte @Nullable [] signature, @NonNull SeenMessages seenMessages) {
        this(message, timestamp, salt, signature == null ? null : new UnmodifiableByteArray(signature), seenMessages);
    }

    /**
     * Constructs the {@linkplain ClientSignedChatMessagePlayPacket signed chat message play packet}.
     *
     * @param message the message
     * @param timestamp a timestamp that the chat message was sent at
     * @param salt a salt used to verify the signature hash
     * @param signature the signature, {@code null} if not present
     * @param seenMessages last messages, which were sent by the client
     * @since 1.0
     */
    public ClientSignedChatMessagePlayPacket {
        NullabilityUtil.requireNonNull(message, "message");
        NullabilityUtil.requireNonNull(seenMessages, "seen messages");
    }
}