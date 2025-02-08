package net.hypejet.jet.event.events.world;

import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event, which is called when {@linkplain World a world} has been changed
 * for {@linkplain Player a player}.
 *
 * @param player a player that the world has been changed to
 * @param previousWorld a world that the player was previously in
 * @param newWorld a world that the player is now in
 * @param startingPosition a new position of the player that has been set when the world was changed
 * @since 1.0
 * @see World
 */
public record WorldSwitchEvent(@NonNull Player player, @NonNull World previousWorld, @NonNull World newWorld,
                               @NonNull Position startingPosition) {
    /**
     * Constructs the {@linkplain WorldSwitchEvent world switch event}.
     *
     * @param player a player that the world has been changed to
     * @param previousWorld a world that the player was previously in
     * @param newWorld a world that the player is now in
     * @param startingPosition a new position of the player that has been set when the world was changed
     * @since 1.0
     */
    public WorldSwitchEvent {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(previousWorld, "previous world");
        NullabilityUtil.requireNonNull(newWorld, "new world");
        NullabilityUtil.requireNonNull(startingPosition, "starting position");
    }
}