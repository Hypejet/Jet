package net.hypejet.jet.player.profile.properties;

import net.hypejet.jet.network.NetworkWritable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Represents properties of a Minecraft game profile.
 *
 * @since 1.0
 * @author Codestech
 * @see NetworkWritable
 */
public sealed interface GameProfileProperties extends NetworkWritable permits GameProfilePropertiesImpl {
    /**
     * Gets a unique identifier of a Minecraft player.
     *
     * @return the unique identifier
     * @since 1.0
     */
    @NonNull UUID uniqueId();

    /**
     * Gets a username of a Minecraft player.
     *
     * @return the username
     * @since 1.0
     */
    @NonNull String username();

    /**
     * Gets a signature of a Minecraft player, which is optional.
     *
     * @return the signature, which may be null
     * @since 1.0
     */
    @Nullable String signature();

    /**
     * Creates a new {@linkplain Builder game profile builder}.
     *
     * @return the game profile builder
     * @since 1.0
     */
    static @NonNull Builder builder() {
        return new GameProfilePropertiesImpl.Builder();
    }

    /**
     * Represents a builder of {@linkplain GameProfileProperties game profile properties}.
     *
     * @since 1.0
     * @author Codestech
     */
    sealed interface Builder permits GameProfilePropertiesImpl.Builder {
        /**
         * Sets a unique identifier of a Minecraft player.
         *
         * @param uniqueId the unique id
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder uniqueId(@NonNull UUID uniqueId);

        /**
         * Sets a username of a Minecraft player.
         *
         * @param username the username
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder username(@NonNull String username);

        /**
         * Sets an optional signature of a Minecraft player.
         *
         * @param signature the signature, which may be null
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder signature(@Nullable String signature);

        /**
         * Builds the {@linkplain GameProfileProperties game profile properties}
         *
         * @return the game profile properties
         * @since 1.0
         */
        @NonNull GameProfileProperties build();
    }
}