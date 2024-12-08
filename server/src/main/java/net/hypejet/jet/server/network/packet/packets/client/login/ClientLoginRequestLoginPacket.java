package net.hypejet.jet.server.network.packet.packets.client.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

/**
 * Represents {@linkplain ClientPacket a client packet} that provides a data of a player of the client to a server.
 *
 * @param username a username of the player
 * @param uniqueId a unique identifier of the player
 * @since 1.0
 * @author Codestech
 */
public record ClientLoginRequestLoginPacket(@NonNull String username, @NonNull UUID uniqueId)
        implements ClientPacket {
    /**
     * Constructs the {@linkplain ClientLoginRequestLoginPacket client login request login packet}.
     *
     * @param username a username of the player
     * @param uniqueId a unique identifier of the player
     * @since 1.0
     */
    public ClientLoginRequestLoginPacket {
        NullabilityUtil.requireNonNull(username, "username");
        NullabilityUtil.requireNonNull(uniqueId, "unique identifier");
    }
}