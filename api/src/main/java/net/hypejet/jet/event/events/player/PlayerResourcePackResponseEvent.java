package net.hypejet.jet.event.events.player;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.pack.ResourcePackResult;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents an event called when a {@linkplain Player player} sends a response of resource pack load.
 *
 * @param player the player
 * @param uniqueId an unique identifier of the resource pack
 * @param result a result of the resource pack
 * @since 1.0
 * @author Codestech
 * @see ResourcePackResult
 */
public record PlayerResourcePackResponseEvent(@NonNull Player player, @NonNull UUID uniqueId,
                                              @NonNull ResourcePackResult result) {}