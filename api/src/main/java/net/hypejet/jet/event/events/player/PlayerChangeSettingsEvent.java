package net.hypejet.jet.event.events.player;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.event.events.CancellableEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a {@linkplain CancellableEvent cancellable event}, which is called when {@linkplain Player.Settings
 * player settings} are changed for a {@linkplain Player player}.
 *
 * @since 1.0
 * @author Codestech
 * @see Player.Settings
 * @see Player
 */
public final class PlayerChangeSettingsEvent extends CancellableEvent {

    private final Player player;
    private final Player.Settings settings;

    /**
     * Constructs the {@linkplain PlayerChangeSettingsEvent player set settings event}.
     *
     * @param player the player, for which the settings are changed
     * @param settings the new settings
     * @since 1.0
     */
    public PlayerChangeSettingsEvent(@NonNull Player player, Player.@NonNull Settings settings) {
        this.player = player;
        this.settings = settings;
    }

    /**
     * Gets the player, for which the settings are changed.
     *
     * @return the player
     * @since 1.0
     */
    public @NonNull Player player() {
        return this.player;
    }

    /**
     * Gets the new settings.
     *
     * @return the settings
     * @since 1.0
     */
    public Player.@NonNull Settings settings() {
        return this.settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerChangeSettingsEvent that)) return false;
        return Objects.equals(this.player, that.player) && Objects.equals(this.settings, that.settings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.player, this.settings);
    }

    @Override
    public String toString() {
        return "PlayerChangeSettingsEvent{" +
                "player=" + this.player +
                ", settings=" + this.settings +
                '}';
    }
}