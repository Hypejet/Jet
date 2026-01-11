package net.hypejet.jet.util.game.audience;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.coordinate.floats.FloatVector;
import net.hypejet.jet.world.particle.Particle;
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

    /**
     * Spawns the specified {@linkplain Particle particle} for this {@linkplain PlayerAudience player audience}.
     *
     * <p>The {@linkplain Particle particle} is not going to override the particle limiter, nor have a chance
     * to spawn when a player has selected minimal or decreased particle spawning strategy in settings.
     * It is going to be spawned with {@code 1} quantity,
     * offset of {@link FloatVector#ZERO} and {@code 0} maximum speed.</p>
     *
     * @param position the position to spawn the particle at
     * @param particle the particle to spawn
     * @since 1.0
     * @see Particle
     */
    void spawnParticle(Vector position, Particle particle);

    /**
     * Spawns the specified {@linkplain Particle particle} for this {@linkplain PlayerAudience player audience}.
     *
     * @param overrideLimiter whether the particle should spawn even if it is going to be spawned
     *                        in a long distance from the player or the player has selected
     *                        minimal particle spawning strategy in settings
     * @param alwaysShow whether the particle should have a chance of being displayed even if the player
     *                   has selected minimal or decreased particle spawning strategy in settings
     * @param position the position to spawn the particle at
     * @param offset size of a cuboid region (centered with the particle original position) that
     *               the particle should spawn in, 1 unit represents 6 blocks
     * @param maxSpeed speed that the particle should move with
     * @param count the number of times that the particle should be spawned
     * @param particle the particle to spawn
     * @since 1.0
     * @see Particle
     */
    void spawnParticle(boolean overrideLimiter, boolean alwaysShow, Vector position,
                       FloatVector offset, float maxSpeed, int count, Particle particle);
}