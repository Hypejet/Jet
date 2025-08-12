package net.hypejet.jet.util.game.crash;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents details of a Minecraft crash report.
 *
 * @param title a title of the details
 * @param description a description of the details
 * @since 1.0
 */
public record CrashReportDetails(@NonNull String title, @NonNull String description) {
    /**
     * Constructs the {@linkplain CrashReportDetails details}.
     *
     * @param title a title of the details
     * @param description a description of the details
     * @since 1.0
     */
    public CrashReportDetails {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(description, "description");
    }
}