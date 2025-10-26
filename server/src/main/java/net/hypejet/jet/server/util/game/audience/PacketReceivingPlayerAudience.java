package net.hypejet.jet.server.util.game.audience;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.Entity.Event;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityAnimationPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityEventPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSpawnParticlePacket;
import net.hypejet.jet.util.game.audience.PlayerAudience;
import net.hypejet.jet.world.coordinate.Vector;
import net.hypejet.jet.world.coordinate.floats.FloatVector;
import net.hypejet.jet.world.particle.Particle;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * A combination of {@linkplain PacketReceivingAudience packet receiving audience}
 * and {@linkplain PlayerAudience player audience}.
 *
 * @since 1.0
 * @see PacketReceivingAudience
 * @see PlayerAudience
 */
@NullMarked
public interface PacketReceivingPlayerAudience extends PacketReceivingCommonAudience, PlayerAudience {
    @Override
    default void animate(Entity entity, Entity.Animation animation) {
        Objects.requireNonNull(entity, "entity");
        Objects.requireNonNull(animation, "animation");
        if (!(entity instanceof JetEntity validatedEntity))
            throw new IllegalArgumentException("The specified entity is not a valid entity");
        this.sendPacket(new ServerEntityAnimationPlayPacket(validatedEntity.entityId(), animation));
    }

    @Override
    default void triggerEvent(Entity entity, Event event) {
        Objects.requireNonNull(entity, "entity");
        Objects.requireNonNull(event, "event");
        if (!(entity instanceof JetEntity validatedEntity))
            throw new IllegalArgumentException("The specified entity is not a valid entity");
        this.sendPacket(new ServerEntityEventPlayPacket(validatedEntity.entityId(), event));
    }

    @Override
    default void spawnParticle(Vector position, Particle particle) {
        this.spawnParticle(false, false, position, FloatVector.ZERO, 0f, 1, particle);
    }

    @Override
    default void spawnParticle(boolean overrideLimiter, boolean alwaysShow, Vector position,
                               FloatVector offset, float maxSpeed, int count, Particle particle) {
        this.sendPacket(new ServerSpawnParticlePacket(
                overrideLimiter, alwaysShow, position,
                offset, maxSpeed, count, particle
        ));
    }
}
