package net.hypejet.jet.server.world.event.events;

import net.hypejet.jet.server.world.event.BooleanWorldEventValueProvider;
import net.hypejet.jet.world.event.world.events.WinWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain BooleanWorldEventValueProvider a boolean world event value provider}, which provides
 * a boolean value for {@linkplain WinWorldEvent a world event}.
 *
 * @since 1.0
 * @see WinWorldEvent
 * @see BooleanWorldEventValueProvider
 */
public final class WinWorldEventValueProvider extends BooleanWorldEventValueProvider<WinWorldEvent> {
    /**
     * Constructs the {@linkplain WinWorldEventValueProvider win world event value provider}.
     *
     * @since 1.0
     */
    public WinWorldEventValueProvider() {
        super((byte) 4);
    }

    @Override
    protected boolean booleanValue(@NonNull WinWorldEvent worldEvent) {
        return worldEvent.rollCredits();
    }
}