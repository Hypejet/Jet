package net.hypejet.jet.server.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Comments;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.Headers;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import net.hypejet.jet.configuration.ServerConfiguration;
import net.hypejet.jet.data.generated.server.FeaturePacks;
import net.hypejet.jet.data.generated.server.MinecraftVersionInfo;
import net.hypejet.jet.data.model.server.pack.FeaturePack;
import net.hypejet.jet.server.network.transport.NettyTransportSelector;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an implementation of {@link ServerConfiguration server configuration} for
 * the {@linkplain net.hypejet.jet.server.JetMinecraftServer Jet Minecraft server}.
 *
 * @since 1.0
 * @author Codestech
 */
@Headers({@Header("A main configuration file for the Jet sever software."), @Header("")})
public final class JetServerConfiguration extends OkaeriConfig implements ServerConfiguration {

    @Comment("An address that the server should bind to.")
    private @MonotonicNonNull String address = defaultAddress();

    @Comment("A port that the server should bind to.")
    private int port = 25565;

    @Comment("A packet length, since which packets are compressed.")
    @CustomKey("compression-threshold")
    private int compressionThreshold = 256;

    @Comments({
            @Comment("A transport, which should be used by netty."),
            @Comment("Do not change if you do not know what it is."),
            @Comment("Available options are NIO, EPOLL, KQUEUE and AUTO."),
            @Comment("AUTO will automatically select the \"best\" available transport.")
    })
    private @MonotonicNonNull NettyTransportSelector transport = NettyTransportSelector.AUTO;

    @Comment("A message used during disconnection when a player is trying to join with an unsupported version.")
    @CustomKey("unsupported-version-message")
    private @MonotonicNonNull Component unsupportedVersionMessage = createDefaultUnsupportedVersionMessage();

    @Comment("A message used as a description of server list ping.")
    @CustomKey("server-list-description")
    private @MonotonicNonNull Component serverListDescription = createDefaultServerListDescription();

    @Comments({
            @Comment("A max amount of the players, which can be on the server at once."),
            @Comment("Note that this value by default is used only by for server list pinging,"),
            @Comment("however, it might be used by other plugins for actually limiting the player count.")
    })
    @CustomKey("max-players")
    private int maxPlayers = 100;

    @Comments({
            @Comment("Feature packs that should be enabled on the server."),
            @Comment,
            @Comment("The feature packs add additional functionality on the server and client"),
            @Comment("Make sure that you put actual Minecraft feature pack keys here.")
    })
    @CustomKey("enabled-packs")
    private @MonotonicNonNull Set<Key> enabledFeaturePacks = createDefaultEnabledFeaturePacks();

    private JetServerConfiguration() {}

    /**
     * Gets an address that the server should bind to.
     *
     * @return the address
     * @since 1.0
     */
    public @NonNull String address() {
        if (this.address == null)
            this.address = defaultAddress();
        return this.address;
    }

    /**
     * Gets a port that the server should bind to.
     *
     * @return the port
     * @since 1.0
     */
    public int port() {
        return this.port;
    }


    /**
     * Gets the {@linkplain NettyTransportSelector netty transport selector}.
     *
     * @return the netty transport selector
     * @since 1.0
     */
    public @NonNull NettyTransportSelector transportSelector() {
        if (this.transport == null)
            this.transport = NettyTransportSelector.AUTO;
        return this.transport;
    }

    /**
     * Gets a {@linkplain Set set} of {@linkplain Key keys} of {@linkplain FeaturePack feature packs} that should
     * be enabled on the server.
     *
     * @return the set
     * @since 1.0
     */
    public @NonNull Set<Key> enabledFeaturePacks() {
        if (this.enabledFeaturePacks == null)
            this.enabledFeaturePacks = createDefaultEnabledFeaturePacks();

        if (!this.enabledFeaturePacks.contains(FeaturePacks.CORE)) {
            Set<Key> enabledPacks = new HashSet<>(this.enabledFeaturePacks);
            enabledPacks.add(FeaturePacks.CORE);
            this.enabledFeaturePacks = Set.copyOf(enabledPacks);
        }

        return Set.copyOf(this.enabledFeaturePacks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compressionThreshold() {
        return this.compressionThreshold;
    }

    /**
     * Gets a message used during disconnection when a player is trying to join with an unsupported version.
     *
     * @return the message
     * @since 1.0
     */
    @Override
    public @NonNull Component unsupportedVersionMessage() {
        if (this.unsupportedVersionMessage == null)
            this.unsupportedVersionMessage = createDefaultUnsupportedVersionMessage();
        return this.unsupportedVersionMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NonNull Component serverListDescription() {
        if (this.serverListDescription == null)
            this.serverListDescription = createDefaultServerListDescription();
        return this.serverListDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int maxPlayers() {
        return this.maxPlayers;
    }

    /**
     * Creates the {@linkplain JetServerConfiguration Jet server configuration}.
     *
     * @return the configuration
     * @since 1.0
     */
    public static @NonNull JetServerConfiguration create() {
        JetServerConfiguration configuration = new JetServerConfiguration();

        configuration.withBindFile(Path.of("server.yaml"));
        configuration.withConfigurer(new YamlSnakeYamlConfigurer(), new JetConfigurationSerdes());
        configuration.withRemoveOrphans(true);
        configuration.saveDefaults();
        configuration.load(true);

        return configuration;
    }

    private static @NonNull Component createDefaultUnsupportedVersionMessage() {
        return Component.text("Unsupported version! Please use " + MinecraftVersionInfo.VERSION_NAME + ".",
                NamedTextColor.DARK_RED, TextDecoration.BOLD);
    }

    private static @NonNull Component createDefaultServerListDescription() {
        return Component.text("A Jet server", NamedTextColor.DARK_PURPLE);
    }

    private static @NonNull String defaultAddress() {
        return "0.0.0.0";
    }

    private static @NonNull Set<Key> createDefaultEnabledFeaturePacks() {
        return Set.of(FeaturePacks.CORE);
    }
}