package net.hypejet.jet.server.network.packet.packets.server.play;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.world.event.GameEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet}, which triggers {@linkplain GameEvent a game event}.
 *
 * @param gameEvent the game event to trigger
 * @since 1.0
 * @see GameEvent
 */
public record ServerGameEventPlayPacket(@NonNull GameEvent gameEvent) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerGameEventPlayPacket server game event play packet}.
     *
     * @param gameEvent the game event to trigger
     * @since 1.0
     */
    public ServerGameEventPlayPacket {
        NullabilityUtil.requireNonNull(gameEvent, "game event");
    }
}