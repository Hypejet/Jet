package net.hypejet.jet.event.events.entity.player;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * An event called just after a {@linkplain Player player} has joined
 * the {@linkplain MinecraftServer server} and has been fully initialized on the server side.
 *
 * @param player the player that joined the server
 * @since 1.0
 * @see MinecraftServer
 * @see Player
 */
public record PlayerJoinEvent(@NonNull Player player) {
    /**
     * Constructs the {@linkplain PlayerJoinEvent player join event}.
     *
     * @param player the player that joined the server
     * @since 1.0
     */
    public PlayerJoinEvent {
        Objects.requireNonNull(player, "player");
    }
}