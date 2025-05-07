package net.hypejet.jet.event.events.brand;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.network.PlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an event called when a brand name is changed for a client associated
 * with {@linkplain PlayerConnection a player connection}.
 *
 * @param connection the player connection
 * @param clientBrand the new client brand
 * @since 1.0
 * @see Player
 */
public record ChangeClientBrandEvent(@NonNull PlayerConnection connection, @NonNull String clientBrand) {
    /**
     * Constructs the {@linkplain ChangeClientBrandEvent change client brand event}.
     *
     * @param connection the player connection
     * @param clientBrand the new client brand
     * @since 1.0
     */
    public ChangeClientBrandEvent {
        NullabilityUtil.requireNonNull(connection, "connection");
        NullabilityUtil.requireNonNull(clientBrand, "client brand");
    }
}