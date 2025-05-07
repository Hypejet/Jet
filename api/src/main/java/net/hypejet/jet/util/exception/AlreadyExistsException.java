package net.hypejet.jet.util.exception;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents {@linkplain RuntimeException a runtime exception}, which is thrown when something already exists,
 * depending on context.
 *
 * @since 1.0
 * @see RuntimeException
 */
public class AlreadyExistsException extends RuntimeException {
    /**
     * Constructs the {@linkplain AlreadyExistsException already exists exception}.
     *
     * @param message a detail message that the exception should have, {@code null} if none
     * @since 1.0
     */
    public AlreadyExistsException(@Nullable String message) {
        super(message);
    }

    /**
     * Constructs the {@linkplain AlreadyExistsException already exists exception}.
     *
     * @param message a detail message that the exception should have, {@code null} if none
     * @param cause a cause of the exception, {@code null} if none
     * @since 1.0
     */
    public AlreadyExistsException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs the {@linkplain AlreadyExistsException already exists exception}.
     *
     * @param cause a cause of the exception, {@code null} if none
     * @since 1.0
     */
    public AlreadyExistsException(@Nullable Throwable cause) {
        super(cause);
    }

    /**
     * Constructs the {@linkplain AlreadyExistsException already exists exception}.
     *
     * @param message a detail message that the exception should have, {@code null} if none
     * @param cause a cause of the exception, {@code null} if none
     * @param enableSuppression whether suppression should be enabled
     * @param writableStackTrace whether the stack trace should be writable
     * @since 1.0
     */
    public AlreadyExistsException(@Nullable String message, @Nullable Throwable cause,
                                  boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Constructs the {@linkplain AlreadyExistsException already exists exception}.
     *
     * @since 1.0
     */
    public AlreadyExistsException() {}
}
