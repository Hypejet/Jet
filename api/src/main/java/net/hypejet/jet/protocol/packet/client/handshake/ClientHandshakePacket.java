package net.hypejet.jet.protocol.packet.client.handshake;

import net.hypejet.jet.player.PlayerConnection;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ClientPacket client packet} that initializes connection of a player.
 *
 * @since 1.0
 * @author Codestech
 */
public interface ClientHandshakePacket extends ClientPacket {
    /**
     * Gets a version of the Minecraft protocol.
     *
     * @return the version
     * @since 1.0
     */
    int protocolVersion();

    /**
     * Gets an address of a server that the client tries to connect to.
     *
     * @return the address
     * @since 1.0
     */
    @NonNull String serverAddress();

    /**
     * Gets a port of a server that the client tries to connect to.
     *
     * @return the port
     * @since 1.0
     */
    int serverPort();

    /**
     * Gets a next {@linkplain ProtocolState protocol state}, which a {@linkplain PlayerConnection player connection}
     * will switch to.
     *
     * @return the protocol state
     * @since 1.0
     */
    @NonNull ProtocolState nextState();
}