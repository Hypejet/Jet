package net.hypejet.jet.protocol.packet.server.login.success;

import net.hypejet.jet.player.profile.properties.GameProfileProperties;
import net.hypejet.jet.protocol.packet.server.login.ServerLoginPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet}, which is sent when a server successfully logs in a
 * player.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public sealed interface ServerLoginSuccessPacket extends ServerLoginPacket permits ServerLoginSuccessPacketImpl {
    /**
     * Gets a unique identifier of a player that is logged in.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();

    /**
     * Gets a username of a player that is logged in.
     *
     * @return the username
     * @since 1.0
     */
    @NonNull String username();

    /**
     * Gets a {@linkplain GameProfileProperties game profile properties} of a player that is logged in.
     *
     * @return the game profile properties
     * @since 1.0
     */
    @NonNull Collection<GameProfileProperties> properties();

    /**
     * Gets whether a player logging in should use strict error handling.
     *
     * @return true if the player logging ing should use strict error handling, false otherwise
     * @since 1.0
     */
    boolean strictErrorHandling();

    /**
     * Creates a new {@linkplain Builder login success packet builder}.
     *
     * @return the builder
     * @since 1.0
     * @see Builder
     */
    static @NonNull Builder builder() {
        return new ServerLoginSuccessPacketImpl.Builder();
    }

    /**
     * Represents a builder of {@linkplain ServerLoginSuccessPacket login success packet}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerLoginSuccessPacket
     */
    sealed interface Builder permits ServerLoginSuccessPacketImpl.Builder {
        /**
         * Sets a unique identifier of a player logging in.
         *
         * @param uniqueId the unique identifier
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder uniqueId(@NonNull UUID uniqueId);

        /**
         * Sets a username of a player logging in.
         *
         * @param username the username
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder username(@NonNull String username);

        /**
         * Sets a collection of {@linkplain GameProfileProperties game profile properties} of a player logging in.
         *
         * @param properties the collection
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder properties(@NonNull Collection<GameProfileProperties> properties);

        /**
         * Sets whether a client should disconnect on invalid server data.
         *
         * @param strictErrorHandling true if a client should disconnect on invalid server data, false otherwise
         * @since 1.0
         * @return the builder
         */
        @NonNull Builder strictErrorHandling(boolean strictErrorHandling);

        /**
         * Builds the {@linkplain ServerLoginSuccessPacket login success packet}.
         *
         * @return the login success packet
         * @since 1.0
         */
        @NonNull ServerLoginSuccessPacket build();

        /**
         * Sets a collection of {@linkplain GameProfileProperties game profile properties} of a player logging in.
         *
         * @param properties the collection
         * @return the builder
         * @since 1.0
         */
        default @NonNull Builder properties(@NonNull GameProfileProperties @NonNull ... properties) {
            return this.properties(List.of(properties));
        }
    }
}