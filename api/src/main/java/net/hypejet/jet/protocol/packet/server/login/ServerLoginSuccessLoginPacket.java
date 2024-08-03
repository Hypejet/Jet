package net.hypejet.jet.protocol.packet.server.login;

import net.hypejet.jet.protocol.packet.server.ServerLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet}, which is sent when a server successfully logs in a
 * player.
 *
 * @param uniqueId a unique identifier of a player that is logged in
 * @param username a username of a player that is logged in
 * @param properties a collection of {@linkplain Property properties} of the login
 * @param strictErrorHandling whether a player logging in should disconnect if this packet is incorrect
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public record ServerLoginSuccessLoginPacket(@NonNull UUID uniqueId, @NonNull String username,
                                            @NonNull Collection<Property> properties, boolean strictErrorHandling)
        implements ServerLoginPacket {
    /**
     * Constructs a {@linkplain ServerLoginSuccessLoginPacket login success packet}.
     *
     * @param uniqueId a unique identifier of a player that is logged in
     * @param username a username of a player that is logged in
     * @param properties a collection of {@linkplain Property properties} of the login
     * @param strictErrorHandling whether a player logging in should disconnect if this packet is incorrect
     * @since 1.0
     */
    public ServerLoginSuccessLoginPacket {
        properties = List.copyOf(properties);
    }

    /**
     * Represents a property of a player login.
     *
     * @param key a key of the property
     * @param value a value of the property
     * @param signature a signature of the property, {@code null} if the property is not signed
     * @since 1.0
     * @author Codestech
     * @see ServerLoginSuccessLoginPacket
     */
    public record Property(@NonNull String key, @NonNull String value, @Nullable String signature) {}
}