package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;

import org.jspecify.annotations.NonNull;

import net.hypejet.jet.entity.Entity;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;

/**
 * A {@linkplain ServerPacket server packet} triggering an {@linkplain Entity entity} event on the client.
 *
 * @param entityId an identifier of the entity to perform the animation on
 * @param event the event to trigger
 * @since 1.0
 * @see Entity
 * @see ServerPacket
 */
public record ServerEntityEventPlayPacket(int entityId, Entity.@NonNull Event event) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerEntityEventPlayPacket server entity event play packet}.
     *
     * @param entityId an identifier of the entity to perform the animation on
     * @param event the event to trigger
     * @since 1.0
     */
    public ServerEntityEventPlayPacket {
        Objects.requireNonNull(event, "entity event");
    }
}
