package net.hypejet.jet.protocol.packet.client.login;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.protocol.packet.server.login.ServerEncryptionRequestLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

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
        implements ClientLoginPacket {}