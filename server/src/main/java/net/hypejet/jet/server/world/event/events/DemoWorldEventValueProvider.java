package net.hypejet.jet.server.world.event.events;

import net.hypejet.jet.server.util.index.IndexUtil;
import net.hypejet.jet.server.world.event.WorldEventValueProvider;
import net.hypejet.jet.world.event.world.events.DemoWorldEvent;
import net.hypejet.jet.world.event.world.events.DemoWorldEvent.Event;
import net.kyori.adventure.util.Index;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * Represents {@linkplain WorldEventValueProvider a world event value provider}, which provides value for
 * {@linkplain DemoWorldEvent a demo world event}.
 *
 * @since 1.0
 * @see DemoWorldEvent
 * @see WorldEventValueProvider
 */
public final class DemoWorldEventValueProvider extends WorldEventValueProvider<DemoWorldEvent> {

    private static final Index<Event, Float> DEMO_EVENT_INDEX = IndexUtil.fromMap(Map.of(
            0F, Event.WELCOME_TO_DEMO_SCREEN,
            101F, Event.TELL_MOVEMENT_CONTROLS,
            102F, Event.TELL_JUMP_CONTROL,
            103F, Event.TELL_INVENTORY_CONTROL,
            104F, Event.DEMO_OVER
    ));

    /**
     * Constructs the {@linkplain DemoWorldEventValueProvider demo world event value provider}.
     *
     * @since 1.0
     */
    public DemoWorldEventValueProvider() {
        super((byte) 5);
    }

    @Override
    public float value(@NonNull DemoWorldEvent worldEvent) {
        return DEMO_EVENT_INDEX.valueOrThrow(worldEvent.event());
    }
}