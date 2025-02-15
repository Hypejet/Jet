package net.hypejet.jet.event.events.configuration;

import net.hypejet.jet.data.model.api.coordinate.Position;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.network.PlayerConnectionState;
import net.hypejet.jet.world.World;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents an event called when {@linkplain Player a player} switches to
 * {@linkplain PlayerConnectionState#CONFIGURATION a configuration connection state}.
 *
 * <p>Note that this event blocks configuration session thread. Unblocking it finishes the session.</p>
 *
 * @since 1.0
 * @see PlayerConnectionState#CONFIGURATION
 */
public final class ConfigurationStartEvent {

    private final Player player;

    private @MonotonicNonNull World spawningWorld;
    private @MonotonicNonNull Position spawningPosition;

    private Player.@Nullable GameMode previousGameMode;
    private Player.GameMode gameMode = Player.GameMode.SURVIVAL;

    private boolean enableRespawnScreen = true;

    /**
     * Constructs the {@linkplain ConfigurationStartEvent player configuration start event}.
     *
     * @param player a player that the configuration has been started for
     * @since 1.0
     */
    public ConfigurationStartEvent(@NonNull Player player) {
        this.player = NullabilityUtil.requireNonNull(player, "player");
    }

    /**
     * Gets {@linkplain Player a player} that the configuration has been started for.
     *
     * @return the player
     * @since 1.0
     */
    public @NonNull Player player() {
        return this.player;
    }

    /**
     * Gets {@linkplain World a world} that the {@linkplain Player player} should spawn in.
     *
     * @return the world, {@code null} if the world has not been set
     * @since 1.0
     */
    public @Nullable World getSpawningWorld() {
        return this.spawningWorld;
    }

    /**
     * Sets {@linkplain World a world} that the {@linkplain Player player} should spawn in.
     *
     * @param world the world
     * @since 1.0
     */
    public void setSpawningWorld(@NonNull World world) {
        this.spawningWorld = NullabilityUtil.requireNonNull(world, "world");
    }

    /**
     * Gets {@linkplain Position a position} where the {@linkplain Player player} should spawn at.
     *
     * @return the position, {@code null} if the position has not been set
     * @since 1.0
     */
    public @Nullable Position getSpawningPosition() {
        return this.spawningPosition;
    }

    /**
     * Sets {@linkplain Position a position} where the {@linkplain Player player} should spawn at.
     *
     * @param position the position
     * @since 1.0
     */
    public void setSpawningPosition(@NonNull Position position) {
        this.spawningPosition = NullabilityUtil.requireNonNull(position, "position");
    }

    /**
     * Gets {@linkplain Player.GameMode a game mode} that the {@linkplain Player player} had before joining the server.
     *
     * @return the game mode, {@code null} if none
     * @since 1.0
     */
    public Player.@Nullable GameMode getPreviousGameMode() {
        return this.previousGameMode;
    }

    /**
     * Sets {@linkplain Player.GameMode a game mode} that the {@linkplain Player player} had before joining the server.
     *
     * @param previousGameMode the game mode that the player had, {@code null} if none
     * @since 1.0
     */
    public void setPreviousGameMode(Player.@Nullable GameMode previousGameMode) {
        this.previousGameMode = NullabilityUtil.requireNonNull(previousGameMode, "previous game mode");
    }

    /**
     * Gets an initial {@linkplain Player.GameMode game mode} that the {@linkplain Player player} should have.
     *
     * @return the game mode
     * @since 1.0
     */
    public Player.@NonNull GameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Sets an initial {@linkplain Player.GameMode game mode} that the {@linkplain Player player} should have.
     *
     * @param gameMode the game mode that the player should have
     * @since 1.0
     */
    public void setGameMode(Player.@NonNull GameMode gameMode) {
        this.gameMode = NullabilityUtil.requireNonNull(gameMode, "game mode");
    }

    /**
     * Gets whether a respawn screen for the {@linkplain Player player} should be enabled.
     *
     * @return {@code true} if the respawn screen should be enabled, {@code false} otherwise
     * @since 1.0
     */
    public boolean shouldEnableRespawnScreen() {
        return this.enableRespawnScreen;
    }

    /**
     * Sets whether a respawn screen for the {@linkplain Player player} should be enabled.
     *
     * @param enableRespawnScreen {@code true} if the respawn screen should be enabled, {@code false} otherwise
     * @since 1.0
     */
    public void setEnableRespawnScreen(boolean enableRespawnScreen) {
        this.enableRespawnScreen = enableRespawnScreen;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ConfigurationStartEvent that)) return false;
        return Objects.equals(this.player, that.player)
                && Objects.equals(this.spawningWorld, that.spawningWorld)
                && Objects.equals(this.spawningPosition, that.spawningPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.player, this.spawningWorld, this.spawningPosition);
    }

    @Override
    public String toString() {
        return "ConfigurationStartEvent{" +
                "player=" + this.player +
                ", world=" + this.spawningWorld +
                ", position=" + this.spawningPosition +
                '}';
    }
}