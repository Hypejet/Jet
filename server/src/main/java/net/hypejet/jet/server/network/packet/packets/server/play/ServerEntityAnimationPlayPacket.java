package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * A {@linkplain ServerPacket server packet} triggering an {@linkplain Entity entity} animation on the client.
 *
 * @param entityId an identifier of the entity to perform the animation on
 * @param animation the animation to play
 * @since 1.0
 * @see Entity
 * @see ServerPacket
 */
public record ServerEntityAnimationPlayPacket(int entityId, Player.@NonNull Animation animation)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerEntityAnimationPlayPacket server entity animation play packet}.
     *
     * @param entityId an identifier of the entity to perform the animation on
     * @param animation the animation to play
     * @since 1.0
     */
    public ServerEntityAnimationPlayPacket {
        Objects.requireNonNull(animation, "animation");
    }
}