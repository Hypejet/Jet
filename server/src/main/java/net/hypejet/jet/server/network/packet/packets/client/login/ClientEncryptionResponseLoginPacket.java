package net.hypejet.jet.server.network.packet.packets.client.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.server.network.packet.packets.server.login.ServerEncryptionRequestLoginPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is a response to an encryption request sent by a server.
 *
 * @param sharedSecret a shared secret value, which is encrypted with a public key of the server
 * @param verifyToken a verify token value, which is encrypted with a public key of the server
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 * @see ServerEncryptionRequestLoginPacket
 */
public record ClientEncryptionResponseLoginPacket(@NonNull UnmodifiableByteArray sharedSecret,
                                                  @NonNull UnmodifiableByteArray verifyToken) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientEncryptionResponseLoginPacket server encryption response login packet}.
     *
     * @param sharedSecret a shared secret value, which is encrypted with a public key of the server
     * @param verifyToken a verify token value, which is encrypted with a public key of the server
     * @since 1.0
     */
    public ClientEncryptionResponseLoginPacket(byte @NonNull [] sharedSecret, byte @NonNull [] verifyToken) {
        this(new UnmodifiableByteArray(sharedSecret), new UnmodifiableByteArray(verifyToken));
    }

    /**
     * Constructs the {@linkplain ClientEncryptionResponseLoginPacket server encryption response login packet}.
     *
     * @param sharedSecret a shared secret value, which is encrypted with a public key of the server
     * @param verifyToken a verify token value, which is encrypted with a public key of the server
     * @since 1.0
     */
    public ClientEncryptionResponseLoginPacket {
        NullabilityUtil.requireNonNull(sharedSecret, "shared secret");
        NullabilityUtil.requireNonNull(verifyToken, "verify token");
    }
}