package net.hypejet.jet.network;

import net.hypejet.concurrency.object.ObjectAcquisition;
import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.player.Player;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a connection with a Minecraft client.
 *
 * @since 1.0
 */
public interface PlayerConnection {
    /**
     * Gets a {@linkplain MinecraftServer minecraft server} owning this connection.
     *
     * @return the minecraft server
     * @since 1.0
     */
    @NonNull MinecraftServer server();

    /**
     * Gets a {@linkplain Player player}, associated with this connection.
     *
     * @return the player. {@code null} if not initialized yet
     * @since 1.0
     */
    @Nullable Player player();

    /**
     * Gets {@linkplain Player a player}, associated with this connection, throws an exception if it has not been
     * initialized yet.
     *
     * @return the player
     * @throws IllegalStateException if the player has not been initialized yet
     * @since 1.0
     */
    @NonNull Player playerOrThrow();

    /**
     * Creates {@linkplain ObjectAcquisition an object acquisition} of {@linkplain PlayerConnectionState a state
     * of the connection}.
     *
     * @return the acquisition
     * @since 1.0
     */
    @NonNull ObjectAcquisition<PlayerConnectionState> connectionState();

    /**
     * Sends a disconnection packet and closes the connection.
     *
     * @param reason a reason of the disconnection
     * @since 1.0
     */
    void disconnect(@NonNull Component reason);
}