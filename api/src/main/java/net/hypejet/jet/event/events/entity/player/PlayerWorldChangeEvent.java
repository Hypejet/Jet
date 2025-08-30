package net.hypejet.jet.event.events.entity.player;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.world.World;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * An event called just after a {@linkplain World world} has been changed for a {@linkplain Player player}.
 *
 * <p>To get the new {@linkplain World world} of the {@linkplain Player player} use {@link Player#world()}.</p>
 *
 * @param player n player that the world has been changed to
 * @param previousWorld a world that the player was in just before the world change
 * @since 1.0
 * @see World
 */
@NullMarked
public record PlayerWorldChangeEvent(Player player, World previousWorld) {
    /**
     * Constructs the {@linkplain PlayerWorldChangeEvent player world change event}.
     *
     * @param player a player that the world has been changed to
     * @param previousWorld a world that the player was in just before the world change
     * @since 1.0
     */
    public PlayerWorldChangeEvent {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(previousWorld, "previous world");
    }
}