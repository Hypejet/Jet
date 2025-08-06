package net.hypejet.jet.server.world.event.events;

import net.hypejet.jet.server.world.event.BooleanWorldEventValueProvider;
import net.hypejet.jet.world.event.world.events.EnableLimitedCraftingWorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain BooleanWorldEventValueProvider a boolean world event value provider}, which provides
 * a boolean value for {@linkplain EnableLimitedCraftingWorldEvent an enable limited crafting world event}.
 *
 * @since 1.0
 * @see EnableLimitedCraftingWorldEvent
 * @see BooleanWorldEventValueProvider
 */
public final class EnableLimitedCraftingWorldEventValueProvider
        extends BooleanWorldEventValueProvider<EnableLimitedCraftingWorldEvent> {
    /**
     * Constructs the {@linkplain EnableLimitedCraftingWorldEventValueProvider enable limited crafting world event
     * value provider}.
     *
     * @since 1.0
     */
    public EnableLimitedCraftingWorldEventValueProvider() {
        super((byte) 12);
    }

    @Override
    protected boolean booleanValue(@NonNull EnableLimitedCraftingWorldEvent worldEvent) {
        return worldEvent.enable();
    }
}