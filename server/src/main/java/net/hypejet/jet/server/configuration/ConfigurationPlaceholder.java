package net.hypejet.jet.server.configuration;

import net.hypejet.jet.server.JetMinecraftServer;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents a placeholder of an unparsed {@linkplain net.kyori.adventure.text.Component component}
 * of {@linkplain net.hypejet.jet.server.configuration.unparsed.UnparsedServerConfiguration an unparsed server
 * configuration}.
 *
 * @since 1.0
 * @see net.kyori.adventure.text.Component
 * @see net.hypejet.jet.server.configuration.unparsed.UnparsedServerConfiguration
 */
public enum ConfigurationPlaceholder {
    /**
     * {@linkplain ConfigurationPlaceholder A configuration placeholder}, which is replaced with a name of a Minecraft
     * version that the servers runs on.
     *
     * @since 1.0
     */
    MINECRAFT_VERSION_NAME("minecraft-version-name") {
        @Override
        @NonNull String value(@NonNull JetMinecraftServer server) {
            return server.minecraftVersion();
        }
    },

    /**
     * {@linkplain ConfigurationPlaceholder A configuration placeholder}, which is replaced with a protocol version of
     * a Minecraft version that the servers runs on.
     *
     * @since 1.0
     */
    MINECRAFT_PROTOCOL_VERSION("minecraft-protocol-version") {
        @Override
        @NonNull String value(@NonNull JetMinecraftServer server) {
            return String.valueOf(server.protocolVersion());
        }
    };

    private final @NonNull @TagPattern String placeholderName;

    /**
     * Constructs the {@linkplain ConfigurationPlaceholder configuration placeholder}.
     *
     * @param placeholderName a name that the placeholder should have
     * @since 1.0
     */
    ConfigurationPlaceholder(@NonNull @TagPattern String placeholderName) {
        this.placeholderName = Objects.requireNonNull(placeholderName, "name");
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
     * Gets value of the placeholder for {@linkplain JetMinecraftServer a Minecraft server specified}.
     *
     * @param server the Minecraft server
     * @return the value
     * @since 1.0
     */
    abstract @NonNull String value(@NonNull JetMinecraftServer server);

    /**
     * Creates {@linkplain TagResolver tag resolvers} for each
     * {@linkplain ConfigurationPlaceholder configuration placeholder} for
     * {@linkplain JetMinecraftServer a Minecraft server} specified.
     *
     * @param server the Minecraft server
     * @return an array of the tag resolvers
     * @since 1.0
     */
    public static @NonNull TagResolver @NonNull [] createTagResolvers(@NonNull JetMinecraftServer server) {
        ConfigurationPlaceholder[] values = values();
        TagResolver[] tagResolvers = new TagResolver[values.length];

        for (int index = 0; index < values.length; index++) {
            ConfigurationPlaceholder value = values[index];
            tagResolvers[index] = Placeholder.unparsed(value.placeholderName(), value.value(server));
        }

        return tagResolvers;
    }
}