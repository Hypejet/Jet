package net.hypejet.jet.server.configuration;

import net.hypejet.jet.server.MinecraftVersion;
import net.hypejet.jet.server.configuration.unparsed.UnparsedServerConfiguration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * A placeholder of an unparsed {@linkplain Component component}
 * from an {@linkplain UnparsedServerConfiguration unparsed server configuration}.
 *
 * @since 1.0
 * @see Component
 * @see UnparsedServerConfiguration
 */
public enum ConfigurationPlaceholder {
    /**
     * A {@linkplain ConfigurationPlaceholder configuration placeholder}
     * that is replaced with a name of a Minecraft version that the servers runs on.
     *
     * @since 1.0
     */
    MINECRAFT_VERSION_NAME("minecraft-version-name", MinecraftVersion.VERSION_NAME),

    /**
     * A {@linkplain ConfigurationPlaceholder configuration placeholder}
     * that is replaced with a protocol version of a Minecraft version that the servers supports.
     *
     * @since 1.0
     */
    MINECRAFT_PROTOCOL_VERSION("minecraft-protocol-version", String.valueOf(MinecraftVersion.PROTOCOL_VERSION));

    private final @TagPattern String placeholderName;
    private final String value;

    /**
     * Constructs the {@linkplain ConfigurationPlaceholder configuration placeholder}.
     *
     * @param placeholderName a name that the placeholder should have
     * @param value a value that the placeholder should have
     * @since 1.0
     */
    ConfigurationPlaceholder(@NonNull @TagPattern String placeholderName, @NonNull String value) {
        this.placeholderName = Objects.requireNonNull(placeholderName, "name");
        this.value = Objects.requireNonNull(value, "value");
    }

    /**
     * Gets a name of this placeholder.
     *
     * @return the name
     * @since 1.0
     */
    public final @NonNull @TagPattern String placeholderName() {
        return this.placeholderName;
    }

    /**
     * Creates an array of {@linkplain TagResolver tag resolvers}
     * resolving {@linkplain ConfigurationPlaceholder configuration placeholders}.
     *
     * @return the tag resolver array
     * @since 1.0
     */
    public static @NonNull TagResolver @NonNull [] createTagResolvers() {
        ConfigurationPlaceholder[] placeholders = values();
        TagResolver[] tagResolvers = new TagResolver[placeholders.length];

        for (int index = 0; index < placeholders.length; index++) {
            ConfigurationPlaceholder placeholder = placeholders[index];
            tagResolvers[index] = Placeholder.unparsed(placeholder.placeholderName(), placeholder.value);
        }

        return tagResolvers;
    }
}