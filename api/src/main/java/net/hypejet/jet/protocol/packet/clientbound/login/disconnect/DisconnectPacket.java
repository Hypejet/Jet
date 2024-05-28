package net.hypejet.jet.protocol.packet.clientbound.login.disconnect;

import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@link ClientBoundPacket client-bound packet} sent by a server to disconnect a player.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientBoundPacket
 */
public sealed interface DisconnectPacket extends ClientBoundPacket permits DisconnectPacketImpl {
    /**
     * Gets a reason of the disconnection.
     *
     * @return the reason
     * @since 1.0
     */
    @NonNull Component reason();

    /**
     * Creates a new {@link Builder disconnect packet builder}.
     *
     * @return the builder
     * @since 1.0
     */
    static @NonNull Builder builder() {
        return new DisconnectPacketImpl.Builder();
    }

    /**
     * Represents a builder that creates a {@link DisconnectPacket disconnect packet}.
     *
     * @since 1.0
     * @author Codestech
     * @see DisconnectPacket
     */
    sealed interface Builder permits DisconnectPacketImpl.Builder {
        /**
         * Sets a reason of the disconnection.
         *
         * @param reason the reason
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder reason(@NonNull Component reason);

        /**
         * Builds the {@link DisconnectPacket disconnect packet}.
         *
         * @return the disconnect packet
         * @since 1.0
         */
        @NonNull DisconnectPacket build();
    }
}