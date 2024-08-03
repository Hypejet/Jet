package net.hypejet.jet.server.session;

import net.hypejet.jet.session.handler.SessionHandler;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Represents a Minecraft temporary session.
 *
 * @param <H> a type of {@linkplain SessionHandler session handler}, which handles this session
 * @since 1.0
 * @author Codestech
 */
public interface Session<H extends SessionHandler> {
    /**
     * A message displayed for a player on a disconnection caused by timing out.
     *
     * @since 1.0
     */
    @NonNull Component TIME_OUT_DISCONNECTION_MESSAGE = Component.text("Timed out");

    /**
     * A common duration of waiting before timing out.
     *
     * @since 1.0
     */
    int TIME_OUT_DURATION = 20;

    /**
     * A common {@linkplain TimeUnit time unit} of waiting before timing out.
     *
     * @since 1.0
     */
    @NonNull TimeUnit TIME_OUT_UNIT = TimeUnit.SECONDS;

    /**
     * Gets a {@linkplain SessionHandler session handler}, which handles this session.
     *
     * @return the session handler
     * @since 1.0
     */
    @NonNull H sessionHandler();

    /**
     * Called when a connection is being closed.
     *
     * @param cause a cause of the closure, {@code null} if not present
     * @since 1.0
     */
    void onConnectionClose(@Nullable Throwable cause);
}