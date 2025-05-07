package net.hypejet.jet.event.events.world;

import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.CancellableEvent;
import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain CancellableEvent a cancellable event}, which is called just before {@linkplain World a world}
 * is being changed for {@linkplain Player a player}.
 *
 * @since 1.0
 * @see World
 */
public final class PreWorldSwitchEvent extends CancellableEvent {

    private final Player player;
    private final World previousWorld;

    private World newWorld;
    private Position startingPosition;

    /**
     * Constructs the {@linkplain PreWorldSwitchEvent pre-world-switch event}.
     *
     * @param player a player that the world is changed for
     * @param previousWorld a world where the player was before the switch
     * @param newWorld a world that the player should be after the switch
     * @param startingPosition a position where the player should be right after the world change
     * @since 1.0
     */
    public PreWorldSwitchEvent(@NonNull Player player, @NonNull World previousWorld, @NonNull World newWorld,
                               @NonNull Position startingPosition) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
        this.previousWorld = NullabilityUtil.requireNonNull(previousWorld, "previous world");
        this.newWorld = NullabilityUtil.requireNonNull(newWorld, "new world");
        this.startingPosition = NullabilityUtil.requireNonNull(startingPosition, "starting position");
    }

    /**
     * Gets {@linkplain Player a player} that the world is changed for.
     *
     * @return the player
     * @since 1.0
     */
    public @NonNull Player player() {
        return this.player;
    }

    /**
     * Gets {@linkplain World a world} where the {@linkplain Player player} was before the switch.
     *
     * @return the world
     * @since 1.0
     */
    public @NonNull World previousWorld() {
        return this.previousWorld;
    }

    /**
     * Gets {@linkplain World a world} where the {@linkplain Player player} should be after the switch.
     *
     * @return the world
     * @since 1.0
     */
    public @NonNull World getNewWorld() {
        return this.newWorld;
    }

    /**
     * Sets {@linkplain World a world} where the {@linkplain Player player} should be after the switch.
     *
     * @param newWorld the new world
     * @since 1.0
     */
    public void setNewWorld(@NonNull World newWorld) {
        this.newWorld = NullabilityUtil.requireNonNull(newWorld, "new world");
    }

    /**
     * Gets {@linkplain Position a position} where the player should be right after the world change.
     *
     * @return the position
     * @since 1.0
     */
    public @NonNull Position getStartingPosition() {
        return this.startingPosition;
    }

    /**
     * Sets {@linkplain Position a position} where the player should be right after the world change.
     *
     * @param position the position
     * @since 1.0
     */
    public void setStartingPosition(@NonNull Position position) {
        this.startingPosition = NullabilityUtil.requireNonNull(position, "position");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreWorldSwitchEvent otherEvent)) return false;
        return Objects.equals(this.player, otherEvent.player)
                && Objects.equals(this.previousWorld, otherEvent.previousWorld)
                && Objects.equals(this.newWorld, otherEvent.newWorld);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.player, this.previousWorld, this.newWorld);
    }

    @Override
    public String toString() {
        return "PreWorldSwitchEvent{" +
                "player=" + this.player +
                ", previousWorld=" + this.previousWorld +
                ", newWorld=" + this.newWorld +
                '}';
    }
}