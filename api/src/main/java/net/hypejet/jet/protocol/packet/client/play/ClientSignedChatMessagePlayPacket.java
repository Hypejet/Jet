package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import net.hypejet.jet.signing.SeenMessages;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Objects;

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
        if (signature != null) {
            signature = signature.clone();
        }
    }

    /**
     * Gets the signature, {@code null} if not present.
     *
     * @return the signature
     * @since 1.0
     */
    @Override
    public byte @Nullable [] signature() {
        byte[] signature = this.signature;
        return signature == null ? null : signature.clone();
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClientSignedChatMessagePlayPacket that)) return false;
        return this.salt == that.salt
                && this.timestamp == that.timestamp
                && Objects.equals(this.message, that.message)
                && Objects.equals(this.seenMessages, that.seenMessages)
                && Objects.deepEquals(this.signature, that.signature);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public int hashCode() {
        return Objects.hash(this.message, this.timestamp, this.salt, Arrays.hashCode(this.signature),
                this.seenMessages);
    }
}