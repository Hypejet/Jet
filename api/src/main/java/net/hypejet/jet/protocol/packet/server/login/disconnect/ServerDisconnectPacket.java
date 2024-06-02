package net.hypejet.jet.protocol.packet.server.login.disconnect;

import net.hypejet.jet.protocol.packet.server.login.ServerLoginPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain ServerLoginPacket server login packet} sent by a server to disconnect a player.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerLoginPacket
 */
public sealed interface ServerDisconnectPacket extends ServerLoginPacket permits ServerDisconnectPacketImpl {
    /**
     * Gets a reason of the disconnection.
     *
     * @return the reason
     * @since 1.0
     */
    @NonNull Component reason();

    /**
     * Creates a new {@linkplain Builder disconnect packet builder}.
     *
     * @return the builder
     * @since 1.0
     */
    static @NonNull Builder builder() {
        return new ServerDisconnectPacketImpl.Builder();
    }

    /**
     * Represents a builder that creates a {@linkplain ServerDisconnectPacket disconnect packet}.
     *
     * @since 1.0
     * @author Codestech
     * @see ServerDisconnectPacket
     */
    sealed interface Builder permits ServerDisconnectPacketImpl.Builder {
        /**
         * Sets a reason of the disconnection.
         *
         * @param reason the reason
         * @return the builder
         * @since 1.0
         */
        @NonNull Builder reason(@NonNull Component reason);

        /**
         * Builds the {@linkplain ServerDisconnectPacket disconnect packet}.
         *
         * @return the disconnect packet
         * @since 1.0
         */
        @NonNull
        ServerDisconnectPacket build();
    }
}