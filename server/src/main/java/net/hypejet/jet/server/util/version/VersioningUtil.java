package net.hypejet.jet.server.util.version;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a utility for parsing version name strings.
 *
 * @since 1.0
 * @author Codestech
 */
public final class VersioningUtil {

    private static final String VERSION_SPLITTER = "\\.";

    private VersioningUtil() {}

    /**
     * Parses a version string into an integer array.
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
            String filteredPart = split[index].chars()
                    .filter(Character::isDigit)
                    .mapToObj(Character::toString)
                    .collect(Collectors.joining());

            int parsedPart = 0;
            if (!filteredPart.isEmpty())
                parsedPart = Integer.parseInt(filteredPart);

            parsed[index] = parsedPart;
        }

        return parsed;
    }
}