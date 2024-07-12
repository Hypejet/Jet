package net.hypejet.jet.protocol.connection;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a Minecraft protocol connection.
 *
 * @since 1.0
 * @author Codestech
 */
public interface PlayerConnection {
    /**
     * Gets a protocol state of the connection.
     *
     * @return the protocol state
     * @since 1.0
     */
    @NonNull ProtocolState getProtocolState();

    /**
     * Sends a {@linkplain ServerPacket client-bound packet} to a client.
     * </p>
     * Note that the packet may be changed in a {@linkplain net.hypejet.jet.event.events.packet.PacketSendEvent packet
     * send event}.
     *
     * @param packet the paket
     * @return the final packet that was sent, null if the event was cancelled, something went wrong during sending
     *         or the connection has been closed
     * @since 1.0
     */
    @Nullable ServerPacket sendPacket(@NonNull ServerPacket packet);

    /**
     * Sends a disconnection packet and closes the connection.
     *
     * @param reason a reason of the disconnection
     * @since 1.0
     */
    void disconnect(@NonNull Component reason);

    /**
     * Gets a compression threshold of the connection, disabled if negative.
     *
     * @return the compression threshold
     * @since 1.0
     */
    int compressionThreshold();

    /**
     * Gets a {@linkplain MinecraftServer minecraft server} owning this connection.
     *
     * @return the minecraft server
     * @since 1.0
     */
    @NonNull MinecraftServer server();

    /**
     * Gets a {@linkplain Player player}, which is using this connection.
     *
     * @return the player. {@code null} if not initialized yet
     * @since 1.0
     */
    @Nullable Player player();

    /**
     * Gets whether the connection has been closed.
     *
     * @return {@code true} if the connection has been closed, {@code false} otherwise
     * @since 1.0
     */
    boolean isClosed();
}