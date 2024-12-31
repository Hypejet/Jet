package net.hypejet.jet.world.event.events;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.world.event.WorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain WorldEvent a world event}, which triggers {@linkplain Event a Minecraft demo event}.
 *
 * @param event the Minecraft demo event
 * @since 1.0
 */
public record DemoWorldEvent(@NonNull Event event) implements WorldEvent {
    /**
     * Constructs the {@linkplain DemoWorldEvent demo world event}.
     *
     * @param event the Minecraft demo event
     * @since 1.0
     */
    public DemoWorldEvent {
        NullabilityUtil.requireNonNull(event, "event");
    }

    /**
     * Represents a Minecraft demo event.
     *
     * <p>This is not an enum, since it depends on Minecraft. Adding an enum entry could break enum switch cases for
     * example</p>
     *
     * @since 1.0
     */
    public static final class Event {
        /**
         * A demo event that will show a welcome screen.
         *
         * @since 1.0
         */
        public static final Event WELCOME_TO_DEMO_SCREEN = new Event("welcome to demo screen");

        /**
         * A demo event that will show movement controls.
         *
         * @since 1.0
         */
        public static final Event TELL_MOVEMENT_CONTROLS = new Event("tell movement controls");

        /**
         * A demo event that will show jump control.
         *
         * @since 1.0
         */
        public static final Event TELL_JUMP_CONTROL = new Event("tell jump control");

        /**
         * A demo event that will show inventory control.
         *
         * @since 1.0
         */
        public static final Event TELL_INVENTORY_CONTROL = new Event("tell inventory control");

        /**
         * A demo event telling that the demo is over and printing a message how to take a screenshot.
         *
         * @since 1.0
         */
        public static final Event DEMO_OVER = new Event("demo over");

        private final String name;

        private Event(@NonNull String name) {
            this.name = NullabilityUtil.requireNonNull(name, "name");
        }

        /**
         * Gets a readable lower-case name of this demo event.
         *
         * @return the name
         * @since 1.0
         */
        public @NonNull String name() {
            return this.name;
        }

        /* Methods #equals and #hashCode are not implemented, since this class is intended to be identity-compared only
           since all instances are defined in constants of this class. */

        @Override
        public String toString() {
            return "Event{" +
                    "name='" + this.name + '\'' +
                    '}';
        }
    }
}