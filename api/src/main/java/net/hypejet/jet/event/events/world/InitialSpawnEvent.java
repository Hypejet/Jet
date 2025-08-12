package net.hypejet.jet.event.events.world;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an event, which is called when an initial {@linkplain World world} has been set
 * for {@linkplain Player a player}.
 *
 * @param player a player that the initial world has been set to
 * @param world the initial world
 * @param startingPosition a new position of the player that has been set when the world was changed
 * @since 1.0
 */
public record InitialSpawnEvent(@NonNull Player player, @NonNull World world, @NonNull Position startingPosition) {
    /**
     * Constructs the {@linkplain InitialSpawnEvent initial spawn event}.
     *
     * @param player a player that the initial world has been set to
     * @param world the initial world
     * @param startingPosition a new position of the player that has been set when the world was changed
     * @since 1.0
     */
    public InitialSpawnEvent {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(startingPosition, "starting position");
    }
}