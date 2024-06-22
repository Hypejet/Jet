package net.hypejet.jet.event.events.player;

import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a {@linkplain Player player} changes a brand of its client.
 *
 * @param player the player
 * @param clientBrand the new client brand
 * @since 1.0
 * @see Player
 */
public record PlayerChangeClientBrandEvent(@NonNull Player player, @NonNull String clientBrand) {}