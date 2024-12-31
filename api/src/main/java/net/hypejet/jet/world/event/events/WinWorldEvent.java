package net.hypejet.jet.world.event.events;

import net.hypejet.jet.world.event.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event} used to indicate that
 * {@linkplain net.hypejet.jet.entity.player.Player a player} on a client won the game.
 *
 * <p>Client respawns after the credits have been rolled or immediately, depending on the {@link #rollCredits}
 * field.</p>
 *
 * @param rollCredits whether the game credits should be rolled
 * @since 1.0
 */
public record WinWorldEvent(boolean rollCredits) implements WorldEvent {}