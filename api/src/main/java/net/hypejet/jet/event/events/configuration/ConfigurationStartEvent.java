package net.hypejet.jet.event.events.configuration;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.scoreboard.Scoreboard;
import net.hypejet.jet.session.configuration.ConfigurationManager;
import net.hypejet.jet.world.World;
import net.hypejet.jet.world.coordinate.Position;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents an event called to configure {@linkplain Player a player} that is going to be created and associated
 * with {@linkplain PlayerConnection a player connection}.
 *
 * <p>Note that this event blocks configuration session thread. Unblocking it finishes the session.</p>
 *
 * @since 1.0
 */
public final class ConfigurationStartEvent {

    private final ConfigurationManager manager;

    private @MonotonicNonNull World spawningWorld;
    private @MonotonicNonNull Position spawningPosition;

    private Player.@Nullable GameMode previousGameMode;
    private Player.@NonNull GameMode gameMode = Player.GameMode.SURVIVAL;

    private boolean enableRespawnScreen = true;
    private @NonNull Scoreboard initialScoreboard;

    /**
     * Constructs the {@linkplain ConfigurationStartEvent configuration start event}.
     *
     * @param manager a manager that the configuration should be handled with
     * @since 1.0
     */
    public ConfigurationStartEvent(@NonNull ConfigurationManager manager) {
        this.manager = Objects.requireNonNull(manager, "manager");
        this.initialScoreboard = manager.connection().server().scoreboardManager().defaultScoreboard();
    }

    /**
     * Gets {@linkplain ConfigurationManager a configuration manager} that the configuration should be handled
     * with.
     *
     * @return the configuration manager
     * @since 1.0
     */
    public @NonNull ConfigurationManager manager() {
        return this.manager;
    }

    /**
     * Gets {@linkplain World a world} that the player should spawn in.
     *
     * @return the world, {@code null} if the world has not been set
     * @since 1.0
     */
    public @Nullable World getSpawningWorld() {
        return this.spawningWorld;
    }

    /**
     * Sets {@linkplain World a world} that the player should spawn in.
     *
     * @param world the world
     * @since 1.0
     */
    public void setSpawningWorld(@NonNull World world) {
        this.spawningWorld = Objects.requireNonNull(world, "world");
    }

    /**
     * Gets {@linkplain Position a position} where the player should spawn at.
     *
     * @return the position, {@code null} if the position has not been set
     * @since 1.0
     */
    public @Nullable Position getSpawningPosition() {
        return this.spawningPosition;
    }

    /**
     * Sets {@linkplain Position a position} where the player should spawn at.
     *
     * @param position the position
     * @since 1.0
     */
    public void setSpawningPosition(@NonNull Position position) {
        this.spawningPosition = Objects.requireNonNull(position, "position");
    }

    /**
     * Gets {@linkplain Player.GameMode a game mode} that the player had before joining the server.
     *
     * @return the game mode, {@code null} if none
     * @since 1.0
     */
    public Player.@Nullable GameMode getPreviousGameMode() {
        return this.previousGameMode;
    }

    /**
     * Sets {@linkplain Player.GameMode a game mode} that the player had before joining the server.
     *
     * @param previousGameMode the game mode that the player had, {@code null} if none
     * @since 1.0
     */
    public void setPreviousGameMode(Player.@Nullable GameMode previousGameMode) {
        this.previousGameMode = Objects.requireNonNull(previousGameMode, "previous game mode");
    }

    /**
     * Gets an initial {@linkplain Player.GameMode game mode} that the player should have.
     *
     * @return the game mode
     * @since 1.0
     */
    public Player.@NonNull GameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Sets an initial {@linkplain Player.GameMode game mode} that the player should have.
     *
     * @param gameMode the game mode that the player should have
     * @since 1.0
     */
    public void setGameMode(Player.@NonNull GameMode gameMode) {
        this.gameMode = Objects.requireNonNull(gameMode, "game mode");
    }

    /**
     * Gets an initial {@linkplain Scoreboard scoreboard} that the {@linkplain Player player} should have.
     *
     * @return the scoreboard
     * @since 1.0
     */
    public @NonNull Scoreboard getInitialScoreboard() {
        return this.initialScoreboard;
    }

    /**
     * Sets an initial {@linkplain Scoreboard scoreboard} that the {@linkplain Player player} should have.
     *
     * @param initialScoreboard the scoreboard that the player should have as their initial scoreboard
     * @since 1.0
     */
    public void setInitialScoreboard(@NonNull Scoreboard initialScoreboard) {
        this.initialScoreboard = Objects.requireNonNull(initialScoreboard, "initial scoreboard");
    }

    /**
     * Gets whether a respawn screen for the player should be enabled.
     *
     * @return {@code true} if the respawn screen should be enabled, {@code false} otherwise
     * @since 1.0
     */
    public boolean shouldEnableRespawnScreen() {
        return this.enableRespawnScreen;
    }

    /**
     * Sets whether a respawn screen for the player should be enabled.
     *
     * @param enableRespawnScreen {@code true} if the respawn screen should be enabled, {@code false} otherwise
     * @since 1.0
     */
    public void setEnableRespawnScreen(boolean enableRespawnScreen) {
        this.enableRespawnScreen = enableRespawnScreen;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConfigurationStartEvent that)) return false;
        return this.enableRespawnScreen == that.enableRespawnScreen
                && Objects.equals(this.spawningWorld, that.spawningWorld)
                && Objects.equals(this.spawningPosition, that.spawningPosition)
                && Objects.equals(this.previousGameMode, that.previousGameMode)
                && Objects.equals(this.gameMode, that.gameMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.spawningWorld, this.spawningPosition,
                this.previousGameMode, this.gameMode, this.enableRespawnScreen);
    }

    @Override
    public String toString() {
        return "ConfigurationStartEvent{" +
                "spawningWorld=" + this.spawningWorld +
                ", spawningPosition=" + this.spawningPosition +
                ", previousGameMode=" + this.previousGameMode +
                ", gameMode=" + this.gameMode +
                ", enableRespawnScreen=" + this.enableRespawnScreen +
                '}';
    }
}