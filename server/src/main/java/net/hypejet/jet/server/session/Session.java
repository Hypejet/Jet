package net.hypejet.jet.server.session;

import net.hypejet.jet.session.handler.SessionHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a Minecraft temporary session.
 *
 * @param <H> a type of {@linkplain SessionHandler session handler}, which handles this session
 * @since 1.0
 * @author Codestech
 */
public interface Session<H extends SessionHandler<?, ?>> {
    /**
     * Gets a {@linkplain SessionHandler session handler}, which handles this session.
     *
     * @return the session handler
     * @since 1.0
     */
    @NonNull H sessionHandler();
}