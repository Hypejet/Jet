package net.hypejet.jet.server.network.packet.packets.server.play;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.event.world.WorldEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which triggers {@linkplain WorldEvent a world event}.
 *
 * @param worldEvent the world event to trigger
 * @since 1.0
 * @see WorldEvent
 */
public record ServerWorldEventPlayPacket(@NonNull WorldEvent worldEvent) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerWorldEventPlayPacket server world event play packet}.
     *
     * @param worldEvent the world event to trigger
     * @since 1.0
     */
    public ServerWorldEventPlayPacket {
        Objects.requireNonNull(worldEvent, "world event");
    }
}