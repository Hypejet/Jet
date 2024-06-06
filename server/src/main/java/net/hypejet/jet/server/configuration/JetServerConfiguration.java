package net.hypejet.jet.server.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Comments;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.Headers;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import net.hypejet.jet.configuration.ServerConfiguration;
import net.hypejet.jet.server.network.transport.NettyTransportSelector;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

/**
 * Represents an implementation of {@link ServerConfiguration server configuration} for
 * the {@linkplain net.hypejet.jet.server.JetMinecraftServer Jet Minecraft server}.
 *
 * @since 1.0
 * @author Codestech
 */
@Headers({@Header("A main configuration file for the Jet sever software"), @Header("")})
public final class JetServerConfiguration extends OkaeriConfig implements ServerConfiguration {

    @Comment("An address that the server should bind to")
    private @MonotonicNonNull String address = "0.0.0.0";

    @Comment("A port that the server should bind to")
    private int port = 25565;

    @Comment("A packet length, since which packets are compressed")
    @CustomKey("compression-threshold")
    private int compressionThreshold = 256;

    @Comments({
            @Comment("A transport, which should be used by netty"),
            @Comment("Do not change if you do not know what it is"),
            @Comment("Available options are NIO, EPOLL, KQUEUE and AUTO"),
            @Comment("AUTO will automatically select the \"best\" available transport")
    })
    private @MonotonicNonNull NettyTransportSelector transport = NettyTransportSelector.AUTO;

    private JetServerConfiguration() {}

    /**
     * Gets an address that the server should bind to.
     *
     * @return the address
     * @since 1.0
     */
    public @NonNull String address() {
        if (this.address == null)
            this.address = "0.0.0.0";
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
     * {@inheritDoc}
     */
    @Override
    public int compressionThreshold() {
        return this.compressionThreshold;
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
        configuration.withConfigurer(new YamlSnakeYamlConfigurer(), new SerdesCommons());
        configuration.withRemoveOrphans(true);
        configuration.saveDefaults();
        configuration.load(true);

        return configuration;
    }
}