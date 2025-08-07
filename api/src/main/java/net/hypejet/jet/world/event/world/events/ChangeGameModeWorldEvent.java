package net.hypejet.jet.world.event.world.events;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.world.event.world.WorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain WorldEvent a world event} used to change {@linkplain Player.GameMode a game mode} for
 * {@linkplain Player a player}.
 *
 * @param gameMode the new game mode
 * @since 1.0
 */
public record ChangeGameModeWorldEvent(Player.@NonNull GameMode gameMode) implements WorldEvent {
    /**
     * Constructs the {@linkplain ChangeGameModeWorldEvent change game mode world event}.
     *
     * @param gameMode the new game mode
     * @since 1.0
     */
    public ChangeGameModeWorldEvent {
        Objects.requireNonNull(gameMode, "game mode");
    }
}