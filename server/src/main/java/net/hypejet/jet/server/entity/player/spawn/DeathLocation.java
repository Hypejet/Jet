package net.hypejet.jet.server.entity.player.spawn;

import java.util.Objects;
import net.hypejet.jet.world.coordinate.BlockPosition;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death location of {@linkplain net.hypejet.jet.entity.player.Player a player}.
 *
 * @param deathDimensionTypeKey a key of a dimension type of world where the player died
 * @param deathPosition a position where the player died at
 * @since 1.0
 */
public record DeathLocation(@NonNull Key deathDimensionTypeKey, @NonNull BlockPosition deathPosition) {
    /**
     * Constructs the {@linkplain DeathLocation death location}.
     *
     * @param deathDimensionTypeKey a key of a dimension type of world where the player died
     * @param deathPosition a position where the player died at
     * @since 1.0
     */
    public DeathLocation {
        Objects.requireNonNull(deathDimensionTypeKey, "death dimension type key");
        Objects.requireNonNull(deathPosition, "death position");
    }
}