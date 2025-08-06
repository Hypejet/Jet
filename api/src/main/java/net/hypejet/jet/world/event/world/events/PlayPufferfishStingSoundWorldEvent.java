package net.hypejet.jet.world.event.world.events;

import net.hypejet.jet.world.event.world.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which plays a pufferfish sting sound.
 *
 * @since 1.0
 */
public final class PlayPufferfishStingSoundWorldEvent implements WorldEvent {
    /**
     * An instance of {@linkplain PlayPufferfishStingSoundWorldEvent play pufferfish sting sound world event}.
     *
     * @since 1.0
     */
    public static final PlayPufferfishStingSoundWorldEvent INSTANCE = new PlayPufferfishStingSoundWorldEvent();

    private PlayPufferfishStingSoundWorldEvent() {}
}