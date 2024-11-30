package net.hypejet.jet.network.packet.server.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which requests encryption from a client.
 *
 * @param serverId an identifier of a server requesting the encryption
 * @param publicKey a public key of a server requesting the encryption
 * @param verifyToken a verify token
 * @param shouldAuthenticate whether the client should authenticate with Mojang
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public record ServerEncryptionRequestLoginPacket(
        @NonNull String serverId, @NonNull UnmodifiableByteArray publicKey,
        @NonNull UnmodifiableByteArray verifyToken, boolean shouldAuthenticate
) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerEncryptionRequestLoginPacket server encryption request login packet}.
     *
     * @param serverId an identifier of a server requesting the encryption
     * @param publicKey a public key of a server requesting the encryption
     * @param verifyToken a verify token
     * @param shouldAuthenticate whether the client should authenticate with Mojang
     * @since 1.0
     */
    public ServerEncryptionRequestLoginPacket(@NonNull String serverId, byte @NonNull [] publicKey,
                                              byte @NonNull [] verifyToken, boolean shouldAuthenticate) {
        this(serverId, new UnmodifiableByteArray(publicKey),
                new UnmodifiableByteArray(verifyToken), shouldAuthenticate);
    }

    /**
     * Constructs the {@linkplain ServerEncryptionRequestLoginPacket server encryption request login packet}.
     *
     * @param serverId an identifier of a server requesting the encryption
     * @param publicKey a public key of a server requesting the encryption
     * @param verifyToken a verify token
     * @param shouldAuthenticate whether the client should authenticate with Mojang
     * @since 1.0
     */
    public ServerEncryptionRequestLoginPacket {
        NullabilityUtil.requireNonNull(serverId, "server id");
        NullabilityUtil.requireNonNull(publicKey, "public key");
        NullabilityUtil.requireNonNull(verifyToken, "verify token");
    }
}