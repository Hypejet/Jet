package net.hypejet.jet.event.events.brand;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a client brand name is changed for {@linkplain Player a player}.
 *
 * @param player the player
 * @param clientBrand the new client brand
 * @since 1.0
 * @see Player
 */
public record ChangeClientBrandEvent(@NonNull Player player, @NonNull String clientBrand) {
    /**
     * Constructs the {@linkplain ChangeClientBrandEvent change client brand event}.
     *
     * @param player the player
     * @param clientBrand the new client brand
     * @since 1.0
     */
    public ChangeClientBrandEvent {
        NullabilityUtil.requireNonNull(player, "player");
        NullabilityUtil.requireNonNull(clientBrand, "client brand");
    }
}