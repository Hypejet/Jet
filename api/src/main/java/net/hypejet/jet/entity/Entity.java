package net.hypejet.jet.entity;

import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a Minecraft entity.
 *
 * @since 1.0
 */
public interface Entity extends Identified, Pointered, HoverEventSource<HoverEvent.ShowEntity>, Keyed {
    /**
     * Gets a key of type of the entity.
     *
     * @return the key
     * @since 1.0
     */
    @NonNull Key entityType();

    /**
     * Gets an identifier of the entity, which is unique to server.
     *
     * @return the identifier
     * @since 1.0
     */
    int entityId();

    /**
     * Gets {@linkplain UUID a unique identifier} of the entity.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();

    /**
     * Represents a hand of an entity.
     *
     * <p>Contents of this enum depend on Minecraft, however it is safe to keep it an enum, since it is very unlikely
     * to change.</p>
     *
     * @since 1.0
     */
    enum Hand {
        /**
         * A left hand of an entity.
         *
         * @since 1.0
         */
        LEFT,
        /**
         * A right hand of an entity.
         *
         * @since 1.0
         */
        RIGHT
    }
}