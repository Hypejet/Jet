package net.hypejet.jet.protocol.packet.client.handshake;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet} that initializes connection of a player.
 *
 * @param protocolVersion a version of Minecraft protocol that the client uses
 * @param serverAddress an address of a server that the client tries to connect to
 * @param serverPort a port of a server that the client tries to connect to
 * @param intent an intent of the client during handshaking
 * @since 1.0
 * @author Codestech
 */
public record ClientHandshakePacket(int protocolVersion, @NonNull String serverAddress, int serverPort,
                                    @NonNull HandshakeIntent intent) implements ClientPacket {
    /**
     * {@inheritDoc}
     */
    @Override
    public @NonNull ProtocolState state() {
        return ProtocolState.HANDSHAKE;
    }

    /**
     * Represents an intent of the client during handshaking.
     *
     * @since 1.0
     * @author Codestech
     */
    public enum HandshakeIntent {
        /**
         * An intent, where the client wants to switch to the {@linkplain ProtocolState#STATUS status protocol state}.
         *
         * @since 1.0
         */
        STATUS,
        /**
         * An intent, where the client wants to switch to the {@linkplain ProtocolState#LOGIN login protocol state}.
         *
         * @since 1.0
         */
        LOGIN,
        /**
         * An intent used by a client as an information that it was transferred from another server.
         *
         * @since 1.0
         */
        TRANSFER
    }
}