package net.hypejet.jet.server.command.exceptions;

import org.jetbrains.annotations.Nullable;

/**
 * Represents {@linkplain RuntimeException a runtime exception}, which is thrown when an error occurs during parsing
 * a command input.
 *
 * @since 1.0
 * @see RuntimeException
 */
public final class CommandParseException extends RuntimeException {
    /**
     * Constructs the {@linkplain CommandParseException command parse exception}.
     *
     * @param message a message of the exception, {@code null} if none
     * @param cause a cause of the exception, {@code null} if none
     */
    public CommandParseException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}