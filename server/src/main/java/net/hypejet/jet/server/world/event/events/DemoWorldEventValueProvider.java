package net.hypejet.jet.server.world.event.events;

import net.hypejet.jet.data.codecs.util.mapper.Mapper;
import net.hypejet.jet.server.world.event.WorldEventValueProvider;
import net.hypejet.jet.world.event.world.events.DemoWorldEvent;
import net.hypejet.jet.world.event.world.events.DemoWorldEvent.Event;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain WorldEventValueProvider a world event value provider}, which provides value for
 * {@linkplain DemoWorldEvent a demo world event}.
 *
 * @since 1.0
 * @see DemoWorldEvent
 * @see WorldEventValueProvider
 */
public final class DemoWorldEventValueProvider extends WorldEventValueProvider<DemoWorldEvent> {

    private static final Mapper<Event, Float> DEMO_EVENT_MAPPER = Mapper.builder(Event.class, float.class)
            .register(Event.WELCOME_TO_DEMO_SCREEN, 0F)
            .register(Event.TELL_MOVEMENT_CONTROLS, 101F)
            .register(Event.TELL_JUMP_CONTROL, 102F)
            .register(Event.TELL_INVENTORY_CONTROL, 103F)
            .register(Event.DEMO_OVER, 104F)
            .build();

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
        Event demoEvent = worldEvent.event();
        Float value = DEMO_EVENT_MAPPER.write(demoEvent);

        if (value == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find a value representing demo event of %s",
                    demoEvent
            ));
        }

        return value;
    }
}