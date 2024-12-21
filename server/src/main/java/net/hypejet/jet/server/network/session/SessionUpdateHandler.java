package net.hypejet.jet.server.network.session;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a function, which is executed when {@linkplain Session a session} is being updated.
 *
 * @since 1.0
 * @see Session
 */
@FunctionalInterface
public interface SessionUpdateHandler {
    /**
     * Handles a session update.
     *
     * @param newSession the new session
     * @since 1.0
     */
    void handleSessionUpdate(@NotNull Session newSession);
}