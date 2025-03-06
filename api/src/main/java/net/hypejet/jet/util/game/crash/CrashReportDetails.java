package net.hypejet.jet.util.game.crash;

import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

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
        NullabilityUtil.requireNonNull(title, "title");
        NullabilityUtil.requireNonNull(description, "description");
    }
}