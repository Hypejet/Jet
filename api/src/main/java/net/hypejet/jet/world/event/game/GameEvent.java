package net.hypejet.jet.world.event.game;

/**
 * A Minecraft game event.
 *
 * @param notificationRadius a square radius within listeners should be notified about the game event, in blocks
 * @since 1.0
 */
public record GameEvent(int notificationRadius) {}