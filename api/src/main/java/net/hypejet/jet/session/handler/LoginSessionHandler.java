package net.hypejet.jet.session.handler;

import net.hypejet.jet.protocol.packet.client.ClientLoginPacket;
import net.hypejet.jet.session.LoginSession;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents something that handles {@linkplain LoginSession a login session}.
 *
 * @since 1.0
 * @author Codestech
 */
public interface LoginSessionHandler {
    /**
     * Handles {@linkplain ClientLoginPacket a client login packet}, which was received from a client.
     *
     * @param packet the client login packet
     * @param session the login session
     * @since 1.0
     */
    default void handlePacket(@NonNull ClientLoginPacket packet, @NonNull LoginSession session) {}

    /**
     * Handles a timeout of the handler, where {@linkplain LoginSession a login session} waits too long for this
     * handler to finish.
     *
     * @param session the login session
     * @since 1.0
     */
    default void handleTimeOut(@NonNull LoginSession session) {}
}