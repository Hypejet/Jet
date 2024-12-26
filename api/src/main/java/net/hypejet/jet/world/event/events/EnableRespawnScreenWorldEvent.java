package net.hypejet.jet.world.event.events;

import net.hypejet.jet.world.event.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which changes whether the respawn screen is enabled on the server.
 *
 * @param enable whether the respawn screen should be enabled
 * @since 1.0
 */
public record EnableRespawnScreenWorldEvent(boolean enable) implements WorldEvent {}