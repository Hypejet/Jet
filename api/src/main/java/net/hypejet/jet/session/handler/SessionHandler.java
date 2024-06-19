package net.hypejet.jet.session.handler;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents something that handles a session of the game.
 *
 * @param <P> a type of packets that this handler handles
 * @param <S> a type of session manager that this handler handles
 * @since 1.0
 * @author Codestech
 */
public interface SessionHandler<P extends ClientPacket, S> {
    /**
     * Called when a packet is received.
     *
     * @param packet the packet
     * @param session a session manager, which uses this handler
     * @since 1.0
     */
    default void onGenericPacket(@NonNull P packet, @NonNull S session) {}

    /**
     * Called when a connection is being closed.
     *
     * @param cause a cause of the closure, {@code null} if not present
     * @since 1.0
     */
    default void onConnectionClose(@Nullable Throwable cause) {}
}