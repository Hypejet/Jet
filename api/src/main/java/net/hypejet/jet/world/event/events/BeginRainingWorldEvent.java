package net.hypejet.jet.world.event.events;

import net.hypejet.jet.world.event.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which starts playing a raining effect.
 *
 * @since 1.0
 */
public final class BeginRainingWorldEvent implements WorldEvent {
    /**
     * An instance of the {@linkplain BeginRainingWorldEvent begin raining world event}.
     *
     * @since 1.0
     */
    public static final BeginRainingWorldEvent INSTANCE = new BeginRainingWorldEvent();

    private BeginRainingWorldEvent() {}
}