package net.hypejet.jet.entity;

import net.hypejet.jet.data.entity.type.EntityType;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents a {@linkplain Identified identified} and {@linkplain Pointered pointered} Minecraft entity.
 *
 * @since 1.0
 * @author Codestech
 * @see Identified
 * @see Pointered
 */
public interface Entity extends Identified, Pointered, HoverEventSource<HoverEvent.ShowEntity>, Keyed {
    /**
     * Gets a type of the entity.
     *
     * @return the type
     * @since 1.0
     * @see EntityType
     */
    @NonNull EntityType entityType();

    /**
     * Gets an identifier of the entity.
     *
     * @return the identifier
     * @since 1.0
     */
    int entityId();

    /**
     * Gets a {@linkplain UUID unique identifier} of the entity.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();

    enum Hand {
        LEFT,
        RIGHT
    }
}