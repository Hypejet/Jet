package net.hypejet.jet.network;

import net.hypejet.jet.MinecraftServer;
import net.hypejet.jet.acquisition.Acquisition;
import net.hypejet.jet.entity.player.Player;
import net.hypejet.jet.network.packet.server.ServerPacket;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a connection with a Minecraft client.
 *
 * @since 1.0
 * @author Codestech
 */
public interface PlayerConnection {
    /**
     * Creates {@linkplain Acquisition an acquisition} of {@linkplain ProtocolState a protocol state} of
     * the connection.
     *
     * @return the acquisition
     * @since 1.0
     */
    @NonNull Acquisition<ProtocolState> protocolState();

    /**
     * Sends a packet to a client.
     *
     * <p>Note that the packet may be changed in
     * {@linkplain net.hypejet.jet.event.events.packet.PacketSendEvent a packet send event}.</p>
     *
     * @param packet the packet
     * @return a completable future, which contains the result of the operation
     * @since 1.0
     */
    @NonNull CompletableFuture<PacketSendResult> sendPacket(@NonNull ServerPacket packet);

    /**
     * Sends a disconnection packet and closes the connection.
     *
     * @param reason a reason of the disconnection
     * @since 1.0
     */
    void disconnect(@NonNull Component reason);

    /**
     * Gets a {@linkplain MinecraftServer minecraft server} owning this connection.
     *
     * @return the minecraft server
     * @since 1.0
     */
    @NonNull MinecraftServer server();

    /**
     * Gets a {@linkplain Player player}, which is using this connection, throws an exception if it has not been
     * initialized yet.
     *
     * @return the player
     * @throws IllegalStateException if the player has not been initialized yet
     * @since 1.0
     */
    @NonNull Player playerOrThrow();

    /**
     * Gets a {@linkplain Player player}, which is using this connection.
     *
     * @return the player. {@code null} if not initialized yet
     * @since 1.0
     */
    @Nullable Player player();

    /**
     * Gets whether the connection has been closed.
     *
     * @return {@code true} if the connection has been closed, {@code false} otherwise
     * @since 1.0
     */
    boolean isClosed();

    /**
     * Represents a result of sending {@linkplain ServerPacket a server packet}.
     *
     * @since 1.0
     * @see ServerPacket
     */
    sealed interface PacketSendResult {
        /**
         * Represents {@linkplain PacketSendResult a packet send result}, which represents a success.
         *
         * @param finalPacket the final packet that was sent
         * @since 1.0
         * @see PacketSendResult
         */
        record Success(@NonNull ServerPacket finalPacket) implements PacketSendResult {}

        /**
         * Represents {@linkplain PacketSendResult a packet send result}, which represents a cancellation
         * of the packet sending, which could have been caused by a packet event cancellation or an internal reason.
         *
         * @since 1.0
         * @see PacketSendResult
         */
        final class Cancellation implements PacketSendResult {
            private static final Cancellation INSTANCE = new Cancellation();
            private Cancellation() {}
        }

        /**
         * Represents {@linkplain PacketSendResult a packet send result}, which represents a network error, which has
         * been already handled.
         *
         * @since 1.0
         * @see PacketSendResult
         */
        final class NetworkError implements PacketSendResult {
            private static final NetworkError INSTANCE = new NetworkError();
            private NetworkError() {}
        }


        /**
         * Gets an instance of {@linkplain Cancellation a cancellation}.
         *
         * @return the instance
         * @since 1.0
         */
        static @NonNull Cancellation cancellation() {
            return Cancellation.INSTANCE;
        }

        /**
         * Gets an instance of {@linkplain NetworkError a network error}.
         *
         * @return the instance
         * @since 1.0
         */
        static @NonNull NetworkError networkError() {
            return NetworkError.INSTANCE;
        }
    }
}