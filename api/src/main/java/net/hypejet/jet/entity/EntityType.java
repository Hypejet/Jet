package net.hypejet.jet.entity;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.key.Keyed;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a type of {@linkplain Entity entity}.
 *
 * @since 1.0
 * @author Codestech
 * @see Entity
 * @see Keyed
 * @see Key
 */
// TODO: Move me to a data gen!
public enum EntityType implements Keyed {
    /**
     * Represents a player entity type.
     *
     * @since 1.0
     * @see net.hypejet.jet.entity.player.Player
     */
    PLAYER("minecraft", "player");

    private final Key key;

    /**
     * Constructs an entity type.
     *
     * @param namespace a namespace of the identifier of the entity type
     * @param value a value of the identifier of the entity type
     * @since 1.0
     * @see Key
     */
    EntityType(@KeyPattern.Namespace @NonNull String namespace, @KeyPattern.Value @NonNull String value) {
        this.key = Key.key(namespace, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NonNull Key key() {
        return this.key;
    }
}