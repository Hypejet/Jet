package net.hypejet.jet.server.plugin.version.part;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Represents {@linkplain VersionPart a version part}, which accepts any version part, following the wildcard parts
 * specified.
 *
 * @param parts parts that the wildcard version part should have
 * @since 1.0
 * @see VersionPart
 */
public record WildcardVersionPart(@NonNull List<WildcardPart> parts) implements VersionPart {
    /**
     * Constructs the {@linkplain WildcardVersionPart wildcard version part}.
     *
     * @param parts parts that the wildcard version part should have
     * @since 1.0
     */
    public WildcardVersionPart {
        parts = List.copyOf(Objects.requireNonNull(parts, "parts"));
    }

    @Override
    public boolean supports(@NonNull NumberVersionPart anotherPart) {
        String remainingString = String.valueOf(anotherPart.number());

        ListIterator<WildcardPart> partIterator = this.parts.listIterator();
        while (partIterator.hasNext()) {
            WildcardPart part = partIterator.next();
            switch (part) {
                case WildcardPart.Number number -> {
                    String numberString = String.valueOf(number.number());
                    if (!remainingString.startsWith(numberString))
                        return false;
                    remainingString = remainingString.replaceFirst(numberString, "");
                }
                case WildcardPart.Wildcard ignoredWildcard -> {
                    if (remainingString.isEmpty())
                        return false;

                    WildcardPart.Number nextPart = null;
                    int nextIndex = partIterator.nextIndex();

                    do {
                        if (this.parts.get(nextIndex) instanceof WildcardPart.Number validatedNextPart)
                            nextPart = validatedNextPart;
                        nextIndex++;
                    } while (nextPart == null && this.parts.size() > nextIndex);

                    /* The remaining string is not empty and wildcard is the last part. It means that the rest of the
                       input is always allowed and contents of it do not matter. */
                    if (nextPart == null)
                        return true;

                    String numberString = String.valueOf(nextPart.number());

                    /* Wildcard requires at least one character to be removed from a string, so if the remaining string
                       starts with the number string of the next argument, that counts as a part of the wildcard part,
                       not a number part. */
                    if (remainingString.startsWith(numberString))
                        remainingString = remainingString.replaceFirst(numberString, "");

                    int nextNumberIndex = remainingString.indexOf(numberString);
                    if (nextNumberIndex == -1)
                        return false;

                    remainingString = remainingString.substring(nextNumberIndex);
                }
            }
        }

        return true;
    }

    /**
     * Represents a part of {@linkplain WildcardVersionPart a wildcard version part}.
     *
     * @since 1.0
     * @see WildcardVersionPart
     */
    public sealed interface WildcardPart {
        /**
         * Represents {@linkplain WildcardPart a wildcard part}, which requires a tested version string to contain
         * the exactly same integer as specified at the same position.
         *
         * @param number the integer
         * @since 1.0
         * @see WildcardPart
         */
        record Number(int number) implements WildcardPart {}

        /**
         * Represents {@linkplain WildcardPart a wildcard part}, which accepts all strings at the same position.
         *
         * @since 1.0
         * @see WildcardPart
         */
        final class Wildcard implements WildcardPart {
            /**
             * An instance of the {@linkplain Wildcard wildcard}.
             *
             * @since 1.0
             */
            public static final Wildcard INSTANCE = new Wildcard();

            private Wildcard() {}
        }
    }
}