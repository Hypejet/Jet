package net.hypejet.jet.world.event.events;

import net.hypejet.jet.world.event.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which instructs the client to wait for chunks of a world.
 *
 * @since 1.0
 */
public final class StartWaitingForWorldChunksWorldEvent implements WorldEvent {
    /**
     * An instance of the {@linkplain StartWaitingForWorldChunksWorldEvent start waiting for world chunks world event}.
     *
     * @since 1.0
     */
    public static final StartWaitingForWorldChunksWorldEvent INSTANCE = new StartWaitingForWorldChunksWorldEvent();

    private StartWaitingForWorldChunksWorldEvent() {}
}