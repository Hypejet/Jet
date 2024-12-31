package net.hypejet.jet.server.plugin.version.part;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain VersionPart a version part}, which represents a plain integer of the part with no additional
 * functionality.
 *
 * @param number the number
 * @since 1.0
 * @see VersionPart
 */
public record NumberVersionPart(int number) implements VersionPart {
    @Override
    public boolean supports(@NonNull NumberVersionPart anotherPart) {
        return this.number == anotherPart.number();
    }
}