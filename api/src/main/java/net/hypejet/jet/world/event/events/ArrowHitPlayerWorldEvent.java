package net.hypejet.jet.world.event.events;

import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.world.event.WorldEvent;

/**
 * Represents {@linkplain WorldEvent a world event}, which is sent when {@linkplain Player a player} is being struck by
 * an arrow.
 *
 * @since 1.0
 */
public final class ArrowHitPlayerWorldEvent implements WorldEvent {
    /**
     * An instance of the {@linkplain ArrowHitPlayerWorldEvent arrow hit player world event}.
     *
     * @since 1.0
     */
    public static final ArrowHitPlayerWorldEvent INSTANCE = new ArrowHitPlayerWorldEvent();

    private ArrowHitPlayerWorldEvent() {}
}