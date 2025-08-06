package net.hypejet.jet.world.event.world.events;

import net.hypejet.jet.world.event.world.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which changes a rain level on a client.
 *
 * @param level a value to change the rain level to
 * @since 1.0
 */
public record RainLevelChangeWorldEvent(float level) implements WorldEvent {}