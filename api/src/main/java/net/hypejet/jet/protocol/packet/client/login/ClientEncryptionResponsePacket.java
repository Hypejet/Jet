package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.encryption.ServerEncryptionRequestPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientLoginPacket client login packet}, which is being sent when a client finishes
 * an authentication.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientLoginPacket
 * @see ServerEncryptionRequestPacket
 */
public non-sealed interface ClientEncryptionResponsePacket extends ClientLoginPacket {
    /**
     * Gets a shared secret value, which is encrypted with a public key of the server.
     *
     * @return the value
     * @since 1.0
     */
    byte @NonNull [] sharedSecret();

    /**
     * Gets a verify token value, which is encrypted with a public key of the server.
     *
     * @return the value
     * @since 1.0
     */
    byte @NonNull [] verifyToken();
}