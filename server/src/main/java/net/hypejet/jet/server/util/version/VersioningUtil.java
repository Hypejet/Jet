package net.hypejet.jet.server.util.version;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a utility for parsing version name strings.
 *
 * @since 1.0
 * @author Codestech
 */
public final class VersioningUtil {

    private static final String VERSION_SPLITTER = "\\.";

    private static final char WILDCARD_CHAR = '*';
    private static final int WILDCARD_DIGIT = -1;

    private static final int ALPHABET_START = 65;
    private static final int ALPHABET_END = 90;

    private VersioningUtil() {}

    /**
     * Parses a version string into an integer array.
     *
     * <p>{@code -1} means that a part allows all values.</p>
     *
     * @param versionString the version string
     * @return the integer array
     * @since 1.0
     */
    public static int @NonNull [] parseVersion(@NonNull String versionString) {
        Objects.requireNonNull(versionString, "The version string must not be null");

        String[] split = versionString.split(VERSION_SPLITTER);
        int[] parsed = new int[split.length];

        for (int index = 0; index < split.length; index++) {
            char[] characters = split[index].toCharArray();

            int parsedPart = 0;

            if (characters.length == 1 && characters[0] == WILDCARD_CHAR) {
                parsedPart = WILDCARD_DIGIT;
            } else {
                StringBuilder builder = new StringBuilder();

                for (char character : characters) {
                    if (Character.isDigit(character)) {
                        builder.append(character);
                        continue;
                    }

                    character = Character.toUpperCase(character);
                    if (character >= ALPHABET_START && character <= ALPHABET_END)
                        builder.append(character - ALPHABET_START);
                }

                if (!builder.isEmpty())
                    parsedPart = Integer.parseInt(builder.toString());
            }

            parsed[index] = parsedPart;
        }

        return parsed;
    }

    /**
     * Gets whether a digit is a {@linkplain #WILDCARD_DIGIT wildcard digit}.
     *
     * @param digit the digit
     * @return {@code true} if the digit is a wildcard digit, {@code false} otherwise
     * @since 1.0
     */
    public static boolean isWildcardDigit(int digit) {
        return digit == WILDCARD_DIGIT;
    }
}