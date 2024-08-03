package net.hypejet.jet.event.events.player.configuration;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents an event called when a {@linkplain Player player} is switched to
 * a {@linkplain net.hypejet.jet.protocol.ProtocolState#CONFIGURATION configuration protocol state}.
 *
 * <p>Note that this event blocks a configuration session thread. Unblocking it will finish the session.</p>
 *
 * @since 1.0
 * @see net.hypejet.jet.protocol.ProtocolState#CONFIGURATION
 */
public final class PlayerConfigurationStartEvent {

    private final Player player;

    private @MonotonicNonNull World initialWorld;

    /**
     * Constructs the {@linkplain PlayerConfigurationStartEvent player configuration start event}.
     *
     * @param player a player that the configuration is being started for
     * @since 1.0
     */
    public PlayerConfigurationStartEvent(@NonNull Player player) {
        this.player = Objects.requireNonNull(player, "The player must not be null");
    }

    /**
     * Gets a {@linkplain Player player} that the configuration is being started for.
     *
     * @return the player
     * @since 1.0
     */
    public @NonNull Player getPlayer() {
        return this.player;
    }

    /**
     * Gets an initial world that the {@linkplain Player player} will be spawned in.
     *
     * @return the initial world, {@code null} if not set yet
     * @since 1.0
     */
    public @Nullable World getInitialWorld() {
        return this.initialWorld;
    }

    /**
     * Sets an initial world that the {@linkplain Player player} will be spawned in.
     *
     * @param initialWorld the initial world
     * @since 1.0
     */
    public void setInitialWorld(@NonNull World initialWorld) {
        this.initialWorld = Objects.requireNonNull(initialWorld, "The initial world must not be null");
    }
}