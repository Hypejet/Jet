package net.hypejet.jet.server.world.event.events;

import net.hypejet.jet.server.world.event.BooleanWorldEventValueProvider;
import net.hypejet.jet.world.event.world.events.EnableRespawnScreenWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain BooleanWorldEventValueProvider a boolean world event value provider}, which provides
 * a boolean value for {@linkplain EnableRespawnScreenWorldEvent an enable respawn screen world event}.
 *
 * @since 1.0
 * @see EnableRespawnScreenWorldEvent
 * @see BooleanWorldEventValueProvider
 */
public final class EnableRespawnScreenWorldEventValueProvider
        extends BooleanWorldEventValueProvider<EnableRespawnScreenWorldEvent> {
    /**
     * Constructs the {@linkplain EnableRespawnScreenWorldEventValueProvider enable respawn screen world event value
     * provider}.
     *
     * @since 1.0
     */
    public EnableRespawnScreenWorldEventValueProvider() {
        super((byte) 11);
    }

    @Override
    protected boolean booleanValue(@NonNull EnableRespawnScreenWorldEvent worldEvent) {
        return worldEvent.enable();
    }
}