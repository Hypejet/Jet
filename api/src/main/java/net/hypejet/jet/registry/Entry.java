package net.hypejet.jet.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an entry of a {@linkplain Registry registry}.
 *
 * @since 1.0
 * @author Codestech
 * @see Registry
 */
public interface Entry extends Keyed {
    /**
     * Gets an {@linkplain Key identifier} of the entry.
     *
     * @return the identifier
     * @since 1.0
     */
    @Override
    @NotNull Key key();
}