package net.hypejet.jet.server.network.packet.packets.client.common;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import net.hypejet.jet.pack.ResourcePackState;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents {@linkplain ClientPacket a client packet}, which is sent by a client to determine a state of the resource
 * pack loading.
 *
 * @param uniqueId a unique identifier of the resource pack
 * @param state the state
 * @since 1.0
 * @author Codestech
 * @see ResourcePackState
 * @see ClientPacket
 */
public record ClientResourcePackStatePacket(@NonNull UUID uniqueId, @NonNull ResourcePackState state)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientResourcePackStatePacket client resource pack response packet}.
     *
     * @param uniqueId a unique identifier of the resource pack
     * @param state the state
     * @since 1.0
     */
    public ClientResourcePackStatePacket {
        NullabilityUtil.requireNonNull(uniqueId, "unique id");
        NullabilityUtil.requireNonNull(state, "state");
    }
}