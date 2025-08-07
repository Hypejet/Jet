package net.hypejet.jet.server.network.packet.packets.server.common;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet} providing a reason for a client for a following disconnection.
 *
 * @param reason the reason
 * @since 1.0
 * @see ServerPacket
 */
public record ServerDisconnectPacket(@NonNull Component reason) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerDisconnectPacket server disconnect packet}.
     *
     * @param reason the reason
     * @since 1.0
     */
    public ServerDisconnectPacket {
        Objects.requireNonNull(reason, "reason");
    }
}