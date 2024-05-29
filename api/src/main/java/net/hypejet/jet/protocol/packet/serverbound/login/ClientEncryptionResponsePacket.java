package net.hypejet.jet.protocol.packet.serverbound.login;

import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ServerBoundPacket server-bound packet}, which is being sent from a client when it authenticates.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerBoundPacket
 * @see net.hypejet.jet.protocol.packet.clientbound.login.encryption.EncryptionRequestPacket
 */
public interface ClientEncryptionResponsePacket extends ServerBoundPacket {
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