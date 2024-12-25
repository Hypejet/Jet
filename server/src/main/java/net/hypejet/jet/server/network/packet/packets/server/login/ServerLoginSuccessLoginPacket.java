package net.hypejet.jet.server.network.packet.packets.server.login;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.network.packet.packets.server.ServerPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents {@linkplain ServerPacket a server packet} finishing the login protocol state and providing data
 * of the player that logged in.
 *
 * @param uniqueId a unique identifier of a player that has logged in
 * @param username a username of a player that has logged in
 * @param properties additional properties of the login
 * @since 1.0
 * @see net.hypejet.jet.server.network.ProtocolState#LOGIN
 * @see ServerPacket
 */
public record ServerLoginSuccessLoginPacket(@NonNull UUID uniqueId, @NonNull String username,
                                            @NonNull Collection<Property> properties) implements ServerPacket {
    /**
     * Constructs the {@linkplain ServerLoginSuccessLoginPacket login success packet}.
     *
     * @param uniqueId a unique identifier of a player that has logged in
     * @param username a username of a player that has logged in
     * @param properties additional properties of the login
     * @since 1.0
     */
    public ServerLoginSuccessLoginPacket {
        NullabilityUtil.requireNonNull(uniqueId, "unique id");
        NullabilityUtil.requireNonNull(username, "username");
        properties = List.copyOf(NullabilityUtil.requireNonNull(properties, "properties"));
    }

    /**
     * Represents a property of a player login.
     *
     * @param key a key of the property
     * @param value a value of the property
     * @param signature a signature of the property, {@code null} if the property has not been signed
     * @since 1.0
     */
    public record Property(@NonNull String key, @NonNull String value, @Nullable String signature) {
        /**
         * Constructs the {@linkplain Property property}.
         *
         * @param key a key of the property
         * @param value a value of the property
         * @param signature a signature of the property, {@code null} if the property has not been signed
         * @since 1.0
         */
        public Property {
            NullabilityUtil.requireNonNull(key, "key");
            NullabilityUtil.requireNonNull(value, "value");
        }
    }
}