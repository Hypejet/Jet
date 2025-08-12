package net.hypejet.jet.world.event.world.events;

import net.hypejet.jet.world.event.world.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which displays a message informing a player that they have no
 * respawn point of a block set.
 *
 * @since 1.0
 */
public final class NoRespawnBlockAvailableWorldEvent implements WorldEvent {
    /**
     * An instance of the {@linkplain NoRespawnBlockAvailableWorldEvent no respawn block available world event}.
     *
     * @since 1.0
     */
    public static final NoRespawnBlockAvailableWorldEvent INSTANCE = new NoRespawnBlockAvailableWorldEvent();

    private NoRespawnBlockAvailableWorldEvent() {}
}