package net.hypejet.jet.world.event.world.events;

import net.hypejet.jet.world.event.world.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which changes a thunder level on a client.
 *
 * @param level a value to change the thunder level to
 * @since 1.0
 */
public record ThunderLevelChangeWorldEvent(float level) implements WorldEvent {}