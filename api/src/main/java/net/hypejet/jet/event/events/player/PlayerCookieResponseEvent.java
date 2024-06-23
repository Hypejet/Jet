package net.hypejet.jet.event.events.player;

import net.hypejet.jet.entity.player.Player;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents an event called when a {@linkplain Player player} sends a cookie response.
 *
 * @param player the player
 * @param identifier an identifier of the cookie
 * @param data a data of the cookie, {@code null} if not present
 * @since 1.0
 * @author Codestech
 */
public record PlayerCookieResponseEvent(@NonNull Player player, @NonNull Key identifier, byte @Nullable [] data) {}