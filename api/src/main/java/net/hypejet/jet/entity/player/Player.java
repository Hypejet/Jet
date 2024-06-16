package net.hypejet.jet.entity.player;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an {@linkplain Entity entity}, which is connected via {@linkplain PlayerConnection player connection}.
 *
 * @since 1.0
 * @author Codestech
 * @see Entity
 * @see PlayerConnection
 * @see Audience
 */
public interface Player extends Entity, Audience {
    /**
     * Gets a username of the player.
     *
     * @return the username
     * @since 1.0
     */
    @NotNull String username();

    /**
     * Gets a {@linkplain PlayerConnection player connection} of the player.
     *
     * @return the player connection
     * @since 1.0-
     */
    @NotNull PlayerConnection connection();

    /**
     * A shortcut for {@link PlayerConnection#sendPacket(ServerPacket)}, which is accessed by {@link #connection()}.
     *
     * @param packet the packet
     * @since 1.0
     * @see PlayerConnection#sendPacket(ServerPacket)
     */
    void sendPacket(@NotNull ServerPacket packet);

    /**
     * A shortcut for {@link PlayerConnection#disconnect(Component)}, which is accessed by {@link #connection()}.
     *
     * @param reason the reason of disconnection
     * @since 1.0
     * @see PlayerConnection#disconnect(Component)
     */
    void disconnect(@NotNull Component reason);
}