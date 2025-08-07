package net.hypejet.jet.server.network.packet.packets.server.login;

import java.util.Objects;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import net.hypejet.jet.session.login.profile.GameProfileProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents {@linkplain ServerPacket a server packet} finishing the login protocol state and providing data
 * of {@linkplain net.hypejet.jet.entity.player.Player a player} that logged in.
 *
 * @param uniqueId a unique identifier of the player that has logged in
 * @param username a username of the player that has logged in
 * @param properties properties of a profile that the player should have
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#LOGIN
 * @see ServerPacket
 */
public record ServerLoginSuccessLoginPacket(@NonNull UUID uniqueId, @NonNull String username,
                                            @NonNull Collection<GameProfileProperty> properties)
        implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerLoginSuccessLoginPacket login success packet}.
     *
     * @param uniqueId a unique identifier of the player that has logged in
     * @param username a username of the player that has logged in
     * @param properties properties of a profile that the player should have
     * @since 1.0
     */
    public ServerLoginSuccessLoginPacket {
        Objects.requireNonNull(uniqueId, "unique id");
        Objects.requireNonNull(username, "username");
        properties = List.copyOf(Objects.requireNonNull(properties, "properties"));
    }
}