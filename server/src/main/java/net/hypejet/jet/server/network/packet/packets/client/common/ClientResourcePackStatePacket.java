package net.hypejet.jet.server.network.packet.packets.client.common;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.kyori.adventure.resource.ResourcePackStatus;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client to determine status of the resource
 * pack loading.
 *
 * @param uniqueId a unique identifier of the resource pack
 * @param status the status
 * @since 1.0
 * @see ResourcePackStatus
 * @see ClientPacket
 */
public record ClientResourcePackStatePacket(@NonNull UUID uniqueId, @NonNull ResourcePackStatus status)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientResourcePackStatePacket client resource pack state response packet}.
     *
     * @param uniqueId a unique identifier of the resource pack
     * @param status the status
     * @since 1.0
     */
    public ClientResourcePackStatePacket {
        Objects.requireNonNull(uniqueId, "unique id");
        Objects.requireNonNull(status, "status");
    }
}