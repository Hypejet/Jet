package net.hypejet.jet.server.plugin.version.part;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents part of version string of {@linkplain net.hypejet.jet.server.plugin.JetPlugin a plugin}.
 *
 * @since 1.0
 */
public sealed interface VersionPart permits NumberVersionPart, WildcardVersionPart {
    /**
     * Gets whether a number version part specified satisfies this version part.
     *
     * @param anotherPart the number version part to check
     * @return {@code true} if the number version part satisfies this version part, {@code false} otherwise
     * @since 1.0
     */
    boolean supports(@NonNull NumberVersionPart anotherPart);
}