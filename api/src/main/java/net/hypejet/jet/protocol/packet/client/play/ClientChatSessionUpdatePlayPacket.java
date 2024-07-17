package net.hypejet.jet.protocol.packet.client.play;

import net.hypejet.jet.protocol.packet.client.ClientPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a {@linkplain ClientPlayPacket client play packet}, which is received when a client initializes
 * their chat session.
 *
 * @param sessionId an identifier of the session
 * @param expiresAt the time that the session expires at, defined in epoch milliseconds
 * @param publicKey a public key of the session, encoded with X.509
 * @param keySignature a signature of the session, consists of a unique identifier of a player, a key expiration
 *                     timestamp and a public key data, hashed with SHA-1 and signed using private RSA key
 * @since 1.0
 * @author Codestech
 */
public record ClientChatSessionUpdatePlayPacket(@NonNull UUID sessionId, long expiresAt, byte @NonNull [] publicKey,
                                                byte @NonNull [] keySignature) implements ClientPlayPacket {
    /**
     * Constructs the {@linkplain ClientChatSessionUpdatePlayPacket chat session update play packet}.
     *
     * <p>The public key and key signature are copied to prevent modifications on the record.</p>
     *
     * @param sessionId an identifier of the session
     * @param expiresAt the time that the session expires at, defined in epoch milliseconds
     * @param publicKey a public key of the session, encoded with X.509
     * @param keySignature a signature of the session, consists of a unique identifier of a player, a key expiration
     *                     timestamp and a public key data, hashed with SHA-1 and signed using private RSA key
     */
    public ClientChatSessionUpdatePlayPacket {
        publicKey = publicKey.clone();
        keySignature = keySignature.clone();
    }

    /**
     * Gets a public key of the session, which is encoded with X.509.
     *
     * <p>A copied array is returned to prevent modifications on the original array.</p>
     *
     * @return the public key
     * @since 1.0
     */
    @Override
    public byte @NonNull [] publicKey() {
        return this.publicKey;
    }

    /**
     * Gets a signature of the session, which consists of a unique identifier of a player, a key expiration
     * timestamp and a public key data, hashed with SHA-1 and signed using private RSA key.
     *
     * <p>A copied array is returned to prevent modifications on the original array.</p>
     *
     * @return the signature
     * @since 1.0
     */
    @Override
    public byte @NonNull [] keySignature() {
        return this.keySignature.clone();
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClientChatSessionUpdatePlayPacket that)) return false;
        return this.expiresAt == that.expiresAt
                && Objects.equals(this.sessionId, that.sessionId)
                && Objects.deepEquals(this.publicKey, that.publicKey)
                && Objects.deepEquals(this.keySignature, that.keySignature);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public int hashCode() {
        return Objects.hash(this.sessionId, this.expiresAt, Arrays.hashCode(this.publicKey),
                Arrays.hashCode(this.keySignature));
    }
}