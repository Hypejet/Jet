package net.hypejet.jet.player;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.protocol.packet.server.login.disconnect.ServerDisconnectPacket;
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
     * Gets a protocol state of the connection.
     *
     * @return the protocol state
     * @since 1.0
     */
    @NonNull ProtocolState getProtocolState();

    /**
     * Sends a {@linkplain ServerPacket client-bound packet} to a client.
     *
     * @param packet the paket
     * @since 1.0
     */
    void sendPacket(@NonNull ServerPacket packet);

    /**
     * Sends a {@linkplain ServerDisconnectPacket disconnect packet} and closes the connection.
     *
     * @param reason a reason of the disconnection
     * @since 1.0
     */
    void kick(@NonNull Component reason);

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
}