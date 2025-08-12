package net.hypejet.jet.server.network.exception;

import net.hypejet.jet.server.network.SocketPlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents {@linkplain RuntimeException a runtime exception}, which is thrown when an error
 * with {@linkplain SocketPlayerConnection a socket player connection} occurs, however it has been already handled
 * or is during handling.
 *
 * @since 1.0
 */
public final class NetworkException extends RuntimeException {

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain NetworkException network exception}.
     *
     * @param connection a connection, within the error occurred
     * @param cause the error
     * @since 1.0
     */
    public NetworkException(@NonNull SocketPlayerConnection connection, @NonNull Throwable cause) {
        this(connection, cause, null);
    }

    /**
     * Constructs the {@linkplain NetworkException network exception}.
     *
     * @param connection a connection, within the error occurred
     * @param cause the error
     * @param message an additional message of the exception, {@code null} if none
     * @since 1.0
     */
    public NetworkException(@NonNull SocketPlayerConnection connection,
                            @NonNull Throwable cause, @Nullable String message) {
        super(message, Objects.requireNonNull(cause, "cause"));
        this.connection = Objects.requireNonNull(connection, "connection");
    }

    /**
     * Gets {@linkplain SocketPlayerConnection a socket player connection}, within the error occurred.
     *
     * @return the socket player connection
     * @since 1.0
     */
    public @NonNull SocketPlayerConnection connection() {
        return this.connection;
    }
}