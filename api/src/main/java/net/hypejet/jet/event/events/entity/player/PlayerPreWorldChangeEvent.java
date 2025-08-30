package net.hypejet.jet.event.events.entity.player;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.CancellableEvent;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A {@linkplain CancellableEvent cancellable event} called just before
 * a {@linkplain World world} is being changed for a {@linkplain Player player}.
 *
 * @since 1.0
 * @see World
 * @see Player
 */
@NullMarked
public final class PlayerPreWorldChangeEvent extends CancellableEvent {

    private final Player player;

    private World world;
    private Position startingPosition;

    /**
     * Constructs the {@linkplain PlayerPreWorldChangeEvent player pre-world-change event}.
     *
     * @param player a player that the world is being changed for
     * @param world a world that the player should be in after the change
     * @param startingPosition a position where the player should be right after the world change
     * @since 1.0
     */
    public PlayerPreWorldChangeEvent(Player player, World world, Position startingPosition) {
        this.player = Objects.requireNonNull(player, "player");
        this.world = Objects.requireNonNull(world, "world");
        this.startingPosition = Objects.requireNonNull(startingPosition, "starting position");
    }

    /**
     * Gets a {@linkplain Player player} that the world is being changed for.
     *
     * @return the player
     * @since 1.0
     */
    public Player player() {
        return this.player;
    }

    /**
     * Gets a {@linkplain World world} where the {@linkplain Player player} should be after the change.
     *
     * @return the world
     * @since 1.0
     */
    public World world() {
        return this.world;
    }

    /**
     * Sets a {@linkplain World world} where the {@linkplain Player player} should be after the change.
     *
     * @param world the new world
     * @since 1.0
     */
    public void world(World world) {
        this.world = Objects.requireNonNull(world, "new world");
    }

    /**
     * Gets a {@linkplain Position position} where the {@linkplain Player player}
     * should be right after the world change.
     *
     * @return the position
     * @since 1.0
     */
    public Position getStartingPosition() {
        return this.startingPosition;
    }

    /**
     * Sets a {@linkplain Position position} where the {@linkplain Player player}
     * should be right after the world change.
     *
     * @param startingPosition the new position where the player should be after the world change
     * @since 1.0
     */
    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = Objects.requireNonNull(startingPosition, "starting position");
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlayerPreWorldChangeEvent event)) return false;
        return Objects.equals(this.player, event.player)
                && Objects.equals(this.world, event.world)
                && Objects.equals(this.startingPosition, event.startingPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.player, this.world, this.startingPosition);
    }

    @Override
    public String toString() {
        return "PreWorldSwitchEvent{" +
                "player=" + this.player +
                ", world=" + this.world +
                ", startingPosition=" + this.startingPosition +
                '}';
    }
}