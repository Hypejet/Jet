package net.hypejet.jet.data.json.model.event;

/**
 * A Minecraft game event.
 *
 * @param notificationRadius a square radius within listeners should be notified about the game event, in blocks
 * @since 1.0
 */
public record JsonGameEvent(int notificationRadius) {}