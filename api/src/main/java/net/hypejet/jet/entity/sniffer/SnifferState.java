package net.hypejet.jet.entity.sniffer;

import net.hypejet.jet.entity.Entity;
import org.jspecify.annotations.NonNull;

/**
 * A state of a sniffer {@linkplain Entity entity}.
 *
 * <p>This is not an enum since it depends on Minecraft.
 * Adding new entries could break switch cases for example.</p>
 *
 * @since 1.0
 * @see Entity
 */
public final class SnifferState {
    /**
     * An idling state.
     *
     * @since 1.0
     */
    public static final SnifferState IDLING = new SnifferState("idle");

    /**
     * A feeling-happy state.
     *
     * @since 1.0
     */
    public static final SnifferState FEELING_HAPPY = new SnifferState("feeling_happy");

    /**
     * A scenting state.
     *
     * @since 1.0
     */
    public static final SnifferState SCENTING = new SnifferState("scenting");

    /**
     * A sniffing state.
     *
     * @since 1.0
     */
    public static final SnifferState SNIFFING = new SnifferState("sniffing");

    /**
     * A searching state.
     *
     * @since 1.0
     */
    public static final SnifferState SEARCHING = new SnifferState("searching");

    /**
     * A digging state.
     *
     * @since 1.0
     */
    public static final SnifferState DIGGING = new SnifferState("digging");

    /**
     * A rising state.
     *
     * @since 1.0
     */
    public static final SnifferState RISING = new SnifferState("rising");

    private final String name;

    private SnifferState(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SnifferState{" +
                "name='" + this.name + '\'' +
                '}';
    }
}