package net.hypejet.jet.world.event.world.events;

import net.hypejet.jet.world.event.world.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which changes whether only unlocked recipes should be able to be
 * used.
 *
 * @param enable whether only unlocked recipes should be able to be used
 * @since 1.0
 */
public record EnableLimitedCraftingWorldEvent(boolean enable) implements WorldEvent {}