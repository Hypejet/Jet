package net.hypejet.jet.event.events.player;

import net.hypejet.jet.entity.player.Player;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a {@linkplain Player player} sends a plugin message.
 *
 * @param player the player
 * @param identifier an identifier of the plugin message
 * @param data a data of the plugin message
 * @since 1.0
 * @author Codestech
 * @see Player
 */
public record PlayerPluginMessageEvent(@NonNull Player player, @NonNull Key identifier, byte @NonNull [] data) {}