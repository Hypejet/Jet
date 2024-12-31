package net.hypejet.jet.server.world.event.events;

import net.hypejet.jet.server.world.event.WorldEventValueProvider;
import net.hypejet.jet.world.event.events.RainLevelChangeWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain WorldEventValueProvider a world event value provider}, which provides value for
 * {@linkplain RainLevelChangeWorldEvent a rain level change world event}.
 *
 * @since 1.0
 * @see RainLevelChangeWorldEvent
 * @see WorldEventValueProvider
 */
public final class RainLevelChangeWorldEventValueProvider extends WorldEventValueProvider<RainLevelChangeWorldEvent> {
    /**
     * Constructs the {@linkplain RainLevelChangeWorldEventValueProvider rain level change world event value provider}.
     *
     * @since 1.0
     */
    public RainLevelChangeWorldEventValueProvider() {
        super((byte) 7);
    }

    @Override
    public float value(@NonNull RainLevelChangeWorldEvent worldEvent) {
        return Math.clamp(worldEvent.level(), 0F, 1F);
    }
}