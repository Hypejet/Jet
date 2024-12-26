package net.hypejet.jet.server.world.event.events;

import net.hypejet.jet.server.world.event.WorldEventValueProvider;
import net.hypejet.jet.world.event.events.ThunderLevelChangeWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain WorldEventValueProvider a world event value provider}, which provides value for
 * {@linkplain ThunderLevelChangeWorldEvent a thunder level change world event}.
 *
 * @since 1.0
 * @see ThunderLevelChangeWorldEvent
 * @see WorldEventValueProvider
 */
public final class ThunderLevelChangeWorldEventValueProvider
        extends WorldEventValueProvider<ThunderLevelChangeWorldEvent> {
    /**
     * Constructs the {@linkplain ThunderLevelChangeWorldEventValueProvider thunder level change world event value provider}.
     *
     * @since 1.0
     */
    public ThunderLevelChangeWorldEventValueProvider() {
        super((byte) 8);
    }

    @Override
    public float value(@NonNull ThunderLevelChangeWorldEvent worldEvent) {
        return Math.clamp(worldEvent.level(), 0F, 1F);
    }
}