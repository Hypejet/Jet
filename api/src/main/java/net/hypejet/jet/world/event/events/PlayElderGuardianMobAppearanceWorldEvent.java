package net.hypejet.jet.world.event.events;

import net.hypejet.jet.world.event.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which plays an elder guardian mob appearance effect.
 *
 * @since 1.0
 */
public final class PlayElderGuardianMobAppearanceWorldEvent implements WorldEvent {
    /**
     * An instance of {@linkplain PlayElderGuardianMobAppearanceWorldEvent play elder guardian mob appearance world
     * event}.
     *
     * @since 1.0
     */
    public static final PlayElderGuardianMobAppearanceWorldEvent
            INSTANCE = new PlayElderGuardianMobAppearanceWorldEvent();

    private PlayElderGuardianMobAppearanceWorldEvent() {}
}