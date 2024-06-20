package net.hypejet.jet.session.handler;

import net.hypejet.jet.protocol.packet.client.ClientPacket;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents something that handles a session of the game.
 *
 * @since 1.0
 * @author Codestech
 */
public interface SessionHandler {
    /**
     * Called when a connection is being closed.
     *
     * @param cause a cause of the closure, {@code null} if not present
     * @since 1.0
     */
    default void onConnectionClose(@Nullable Throwable cause) {}
}