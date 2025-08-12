package net.hypejet.jet.server.network.packet.packets.client.handshake;

import net.hypejet.jet.server.network.ProtocolState;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain ClientPacket a client packet} that provides basic information about a player connection.
 *
 * @param protocolVersion a version of Minecraft protocol that the connection uses
 * @param serverAddress an address of a server that the client is connected to
 * @param serverPort a port of a server that the client is connected to
 * @param intent an intent of the client during handshaking
 * @since 1.0
 */
public record ClientHandshakePacket(int protocolVersion, @NonNull String serverAddress, int serverPort,
                                    @NonNull HandshakeIntent intent) implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientHandshakePacket client handshake packet}.
     *
     * @param protocolVersion a version of Minecraft protocol that the connection uses
     * @param serverAddress an address of a server that the client is connected to
     * @param serverPort a port of a server that the client is connected to
     * @param intent an intent of the client during handshaking
     * @since 1.0
     */
    public ClientHandshakePacket {
        Objects.requireNonNull(serverAddress, "server address");
        Objects.requireNonNull(intent, "intent");
    }

    /**
     * Represents an intent of the client during handshaking.
     *
     * @since 1.0
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
         * An intent used by a client as an information that it was transferred from another server and wants
         * to switch to the {@linkplain ProtocolState#STATUS status protocol state}.
         *
         * @since 1.0
         */
        TRANSFER
    }
}