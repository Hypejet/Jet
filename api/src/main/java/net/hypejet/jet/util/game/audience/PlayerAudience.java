package net.hypejet.jet.util.game.audience;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import org.jspecify.annotations.NullMarked;

/**
 * A single {@linkplain Player player} or group of {@linkplain Player players} receiving Minecraft media.
 *
 * @since 1.0
 * @see Player
 */
@NullMarked
public interface PlayerAudience extends CommonAudience {
    /**
     * Triggers an animation of the specified {@linkplain Entity entity}
     * for this {@linkplain PlayerAudience player audience}.
     *
     * @param entity the entity that the animation should be triggered on
     * @param animation the animation to trigger
     * @since 1.0
     */
    void animate(Entity entity, Entity.Animation animation);
    
    /**
     * Triggers an event on the specified {@linkplain Entity entity}
     * for this {@linkplain PlayerAudience player audience}.
     *
     * @param entity the entity that the event should be triggered on
     * @param event the event to trigger
     * @since 1.0
     */
    void triggerEvent(Entity entity, Entity.Event event);
}