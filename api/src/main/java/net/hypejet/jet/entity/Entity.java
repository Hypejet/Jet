package net.hypejet.jet.entity;

import net.hypejet.jet.world.World;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
     * Gets an identifier of type of the entity.
     *
     * @return the identifier
     * @since 1.0
     */
    @NonNull Key entityType();

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

    /**
     * Gets a world, which the entity is in.
     *
     * @return the world, {@code null} if not set yet
     * @since 1.0
     */
    @Nullable World getWorld();

    /**
     * Sets and teleports an entity to a world.
     *
     * @param world the world
     * @since 1.0
     */
    void setWorld(@NonNull World world);

    /**
     * Represents a hand of an entity.
     *
     * @since 1.0
     * @author Codestech
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