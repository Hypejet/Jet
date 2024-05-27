package net.hypejet.jet.player;

import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import net.hypejet.jet.protocol.packet.clientbound.login.DisconnectPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft protocol connection.
 *
 * @since 1.0
 * @author Codestech
 */
public interface PlayerConnection {
    /**
     * Gets a protocol version of the connection, {@code -1} if unknown.
     *
     * @return the protocol version
     * @since 1.0
     */
    int getProtocolVersion();

    /**
     * Gets a protocol state of the connection.
     *
     * @return the protocol state
     * @since 1.0
     */
    @NonNull ProtocolState getProtocolState();

    /**
     * Sends a {@link ClientBoundPacket client-bound packet} to a client.
     *
     * @param packet the paket
     * @since 1.0
     */
    void sendPacket(@NonNull ClientBoundPacket packet);

    /**
     * Sends a {@link DisconnectPacket disconnect packet} and closes the connection.
     *
     * @param reason a reason of the disconnection
     * @since 1.0
     */
    void kick(@NonNull Component reason);
}