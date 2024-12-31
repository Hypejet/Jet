package net.hypejet.jet.server.plugin.version;

import com.google.common.collect.Iterators;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.plugin.version.part.NumberVersionPart;
import net.hypejet.jet.server.plugin.version.part.VersionPart;
import net.hypejet.jet.server.plugin.version.part.WildcardVersionPart;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a utility for parsing plugin version strings.
 *
 * @since 1.0
 * @see net.hypejet.jet.plugin.Plugin
 */
public final class PluginVersionParser {

    private static final char WILDCARD_CHAR = '*';
    private static final String WILDCARD_STRING = String.valueOf(WILDCARD_CHAR);

    private static final String VERSION_SPLITTER = "\\.";
    private static final String WILDCARD_SPLITTER = "\\" + WILDCARD_CHAR;

    private PluginVersionParser() {}

    /**
     * Parses a version string specified into {@linkplain List a list} of {@linkplain VersionPart version parts}.
     *
     * @param versionString the version string to parse
     * @return the list of version parts
     * @since 1.0
     */
    public static @NonNull List<VersionPart> parseVersion(@NonNull String versionString) {
        NullabilityUtil.requireNonNull(versionString, "version string");

        String[] split = versionString.split(VERSION_SPLITTER);
        List<VersionPart> parsed = new ArrayList<>(split.length);

        for (int index = 0; index < split.length; index++) {
            String unparsedVersionPart = split[index];
            if (unparsedVersionPart.isEmpty())
                throw new IllegalArgumentException("A part of a version string cannot be empty");

            VersionPart versionPart = unparsedVersionPart.contains(WILDCARD_STRING)
                    ? parseWildcardVersionPart(unparsedVersionPart)
                    : new NumberVersionPart(parseNumber(unparsedVersionPart));
            parsed.add(index, versionPart);
        }

        return List.copyOf(parsed);
    }

    private static @NonNull WildcardVersionPart parseWildcardVersionPart(@NonNull String unparsedVersionPart) {
        String[] unparsed = unparsedVersionPart.split(WILDCARD_SPLITTER);
        List<WildcardVersionPart.WildcardPart> parsed = new ArrayList<>();

        Iterator<String> splitIterator = Iterators.forArray(unparsed);
        while (splitIterator.hasNext()) {
            String part = splitIterator.next();
            if (!part.isEmpty())
                parsed.add(new WildcardVersionPart.WildcardPart.Number(parseNumber(part)));

            if (splitIterator.hasNext()) {
                // Avoid issues with double wildcards
                if (parsed.getLast() instanceof WildcardVersionPart.WildcardPart.Wildcard) continue;
                parsed.add(WildcardVersionPart.WildcardPart.Wildcard.INSTANCE);
            }
        }

        return new WildcardVersionPart(parsed);
    }

    private static int parseNumber(@NonNull String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    String.format("String %s is not a valid string of a version part", string),
                    exception
            );
        }
    }
}