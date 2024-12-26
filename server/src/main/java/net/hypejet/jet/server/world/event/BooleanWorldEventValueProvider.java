package net.hypejet.jet.server.world.event;

import net.hypejet.jet.world.event.WorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain WorldEventValueProvider a world event value provider}, whose world event value is a boolean.
 *
 * @param <WE> a type of the world event
 * @since 1.0
 * @see WorldEventValueProvider
 */
public abstract class BooleanWorldEventValueProvider<WE extends WorldEvent> extends WorldEventValueProvider<WE> {
    /**
     * Constructs the {@linkplain BooleanWorldEventValueProvider boolean world event value provider}.
     *
     * @param identifier an identifier of the world event that the provider should provide values for
     * @since 1.0
     */
    public BooleanWorldEventValueProvider(byte identifier) {
        super(identifier);
    }

    @Override
    public final float value(@NonNull WE worldEvent) {
        return this.booleanValue(worldEvent) ? 1F : 0F;
    }

    /**
     * Gets a boolean representation of values of the world event specified.
     *
     * @param worldEvent the world event
     * @return the boolean representation
     * @since 1.0
     */
    protected abstract boolean booleanValue(@NonNull WE worldEvent);
}