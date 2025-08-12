package net.hypejet.jet.server.network.packet.packets.client.play;

import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.util.array.UnmodifiableByteArray;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents {@linkplain ClientPacket a client packet}, which initializes a chat session of a client.
 *
 * @param sessionId an identifier of the session
 * @param expiresAt the time that the session expires at, defined in epoch milliseconds
 * @param publicKey a public key of the session, encoded with X.509
 * @param keySignature a signature of the session, consists of a unique identifier of a player, a key expiration
 *                     timestamp and a public key data, hashed with SHA-1 and signed using private RSA key
 * @since 1.0
 */
public record ClientChatSessionUpdatePlayPacket(@NonNull UUID sessionId, long expiresAt,
                                                @NonNull UnmodifiableByteArray publicKey,
                                                @NonNull UnmodifiableByteArray keySignature) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientChatSessionUpdatePlayPacket chat session update play packet}.
     *
     * @param sessionId an identifier of the session
     * @param expiresAt the time that the session expires at, defined in epoch milliseconds
     * @param publicKey a public key of the session, encoded with X.509
     * @param keySignature a signature of the session, consists of a unique identifier of a player, a key expiration
     *                     timestamp and a public key data, hashed with SHA-1 and signed using private RSA key
     * @since 1.0
     */
    public ClientChatSessionUpdatePlayPacket(@NonNull UUID sessionId, long expiresAt, byte @NonNull [] publicKey,
                                             byte @NonNull [] keySignature) {
        this(sessionId, expiresAt, new UnmodifiableByteArray(publicKey), new UnmodifiableByteArray(keySignature));
    }

    /**
     * Constructs the {@linkplain ClientChatSessionUpdatePlayPacket chat session update play packet}.
     *
     * @param sessionId an identifier of the session
     * @param expiresAt the time that the session expires at, defined in epoch milliseconds
     * @param publicKey a public key of the session, encoded with X.509
     * @param keySignature a signature of the session, consists of a unique identifier of a player, a key expiration
     *                     timestamp and a public key data, hashed with SHA-1 and signed using private RSA key
     * @since 1.0
     */
    public ClientChatSessionUpdatePlayPacket {
        Objects.requireNonNull(sessionId, "session id");
        Objects.requireNonNull(publicKey, "public key");
        Objects.requireNonNull(keySignature, "key signature");
    }
}