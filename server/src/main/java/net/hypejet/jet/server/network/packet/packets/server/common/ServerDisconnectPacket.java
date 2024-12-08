package net.hypejet.jet.server.network.packet.packets.server.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain ServerPacket a server packet} providing a reason for a client for a following disconnection.
 *
 * @param reason the reason
 * @since 1.0
 * @author Codestech
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
        NullabilityUtil.requireNonNull(reason, "reason");
    }
}