package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import net.hypejet.jet.signing.SeenMessages;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when
 * a {@link net.hypejet.jet.entity.player.Player player} sends a chat message with a signature.
 *
 * @param message the message
 * @param timestamp a timestamp that the chat message was sent at
 * @param salt a salt used to verify the signature hash
 * @param signature the signature, {@code null} if not present
 * @param seenMessages last messages, which were sent by the client
 * @since 1.0
 * @author Codestech
 * @see ClientPlayPacket
 */
public record ClientSignedChatMessagePlayPacket(@NonNull String message, long timestamp, long salt,
                                                byte @Nullable [] signature, @NonNull SeenMessages seenMessages)
        implements ClientPlayPacket {

    public ClientSignedChatMessagePlayPacket {
        if (signature != null) {
            signature = signature.clone();
        }
    }

    @Override
    public byte @Nullable [] signature() {
        byte[] signature = this.signature;
        return signature == null ? null : signature.clone();
    }
}