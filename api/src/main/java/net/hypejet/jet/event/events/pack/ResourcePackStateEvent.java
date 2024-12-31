package net.hypejet.jet.event.events.pack;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.util.game.pack.ResourcePackState;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents an event called when a client sends a state of resource pack loading.
 *
 * @param player a player associated with the client
 * @param uniqueId a unique identifier of the resource pack that is being loaded
 * @param state the state
 * @since 1.0
 * @see ResourcePackState
 */
public record ResourcePackStateEvent(@NonNull Player player, @NonNull UUID uniqueId,
                                     @NonNull ResourcePackState state) {
    /**
     * Constructs the {@linkplain ResourcePackStateEvent resource pack state event}.
     *
     * @param player a player associated with the client
     * @param uniqueId a unique identifier of the resource pack that is being loaded
     * @param state the state
     * @since 1.0
     */
    public ResourcePackStateEvent {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(uniqueId, "unique identifier");
        NullabilityUtil.requireNonNull(state, "state");
    }
}