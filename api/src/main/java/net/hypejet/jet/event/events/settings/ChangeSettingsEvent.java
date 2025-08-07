package net.hypejet.jet.event.events.settings;

import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents an event, which is called when {@linkplain Player.Settings player settings} are changed for
 * {@linkplain Player a player}.
 *
 * @param player the player, for which the settings are changed
 * @param settings the new settings
 * @since 1.0
 * @see Player.Settings
 * @see Player
 */
public record ChangeSettingsEvent(@NonNull Player player, Player.@NonNull Settings settings) {
    /**
     * Constructs the {@linkplain ChangeSettingsEvent change settings event}.
     *
     * @param player the player, for which the settings are changed
     * @param settings the new settings
     * @since 1.0
     */
    public ChangeSettingsEvent {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(settings, "settings");
    }
}