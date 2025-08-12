package net.hypejet.jet.world.event.world.events;

import net.hypejet.jet.world.event.world.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which stops playing a raining effect.
 *
 * @since 1.0
 */
public final class EndRainingWorldEvent implements WorldEvent {
    /**
     * An instance of the {@linkplain EndRainingWorldEvent end raining world event}.
     *
     * @since 1.0
     */
    public static final EndRainingWorldEvent INSTANCE = new EndRainingWorldEvent();

    private EndRainingWorldEvent() {}
}