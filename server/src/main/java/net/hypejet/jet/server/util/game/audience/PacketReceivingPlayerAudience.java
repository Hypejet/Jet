package net.hypejet.jet.server.util.game.audience;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityAnimationPlayPacket;
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
        this.sendPacket(new ServerEntityAnimationPlayPacket(entity.entityId(), animation));
    }
}