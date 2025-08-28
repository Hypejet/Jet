package net.hypejet.jet.server.util.game.audience;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.Entity.Status;
import net.hypejet.jet.server.entity.JetEntity;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityAnimationPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityEventPlayPacket;
import net.hypejet.jet.util.game.audience.PlayerAudience;
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
    default void status(Entity entity, Status status) {
        Objects.requireNonNull(entity, "entity");
        Objects.requireNonNull(status, "status");
        if (!(entity instanceof JetEntity validatedEntity))
            throw new IllegalArgumentException("The specified entity is not a valid entity");
        this.sendPacket(new ServerEntityEventPlayPacket(validatedEntity.entityId(), status));
    }
}
