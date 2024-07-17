package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet}, which is being sent when a client finishes
 * an authentication.
 *
 * @param sharedSecret a shared secret value, which is encrypted with a public key of the server
 * @param verifyToken a verify token value, which is encrypted with a public key of the server
 * @since 1.0
 * @author Codestech
 * @see ClientLoginPacket
 * @see ServerEncryptionRequestLoginPacket
 */
public record ClientEncryptionResponseLoginPacket(byte @NonNull [] sharedSecret, byte @NonNull [] verifyToken)
        implements ClientLoginPacket {
    /**
     * Constructs the {@linkplain ClientEncryptionResponseLoginPacket encryption response login packet}.
     *
     * <p>The shared secret and verify token arrays are cloned to avoid modifications on the record.</p>
     *
     * @param sharedSecret a shared secret value, which is encrypted with a public key of the server
     * @param verifyToken a verify token value, which is encrypted with a public key of the server
     * @since 1.0
     */
    public ClientEncryptionResponseLoginPacket {
        sharedSecret = sharedSecret.clone();
        verifyToken = verifyToken.clone();
    }

    /**
     * Gets a shared secret value, which is encrypted with a public key of the server.
     *
     * <p>The array returned is a clone to prevent modifications of the original array.</p>
     *
     * @return the value
     * @since 1.0
     */
    @Override
    public byte @NonNull [] sharedSecret() {
        return this.sharedSecret.clone();
    }

    /**
     * Gets a verify token value, which is encrypted with a public key of the server.
     *
     * <p>The array returned is a clone to prevent modifications of the original array.</p>
     *
     * @return the value
     */
    @Override
    public byte @NonNull [] verifyToken() {
        return this.verifyToken.clone();
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClientEncryptionResponseLoginPacket that)) return false;
        return Objects.deepEquals(this.verifyToken, that.verifyToken)
                && Objects.deepEquals(this.sharedSecret, that.sharedSecret);
    }

    /**
     * {@inheritDoc}
     */
    // Override, because records do not compare contents of arrays natively
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(this.sharedSecret), Arrays.hashCode(this.verifyToken));
    }
}