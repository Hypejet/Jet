package net.hypejet.jet.server.configuration;

import net.hypejet.jet.configuration.ServerConfiguration;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.unparsed.UnparsedServerConfiguration;
import net.hypejet.jet.server.network.netty.transport.NettyTransportSelector;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

/**
 * Represents an implementation of the {@linkplain ServerConfiguration server configuration}.
 *
 * @param address an address that the sever should bind to
 * @param port a port that the server should bind to
 * @param compressionThreshold a packet length, since which packets should be compressed
 * @param transportSelector a netty transport selector that the server should use for selecting netty native transport
 * @param unsupportedVersionMessage a message that should be used during disconnection when a client is trying to join
 *                                  with an unsupported version
 * @param serverListDescription a message that should be used as a description of default server list ping
 * @param maximumPlayers a maximum amount of players, which can be on the server at once
 * @param enabledFeaturePacks a set of keys of feature packs that should be enabled on the server
 * @param transfersAllowed whether clients transferred to the server from another server should be able to join the
 *                         server
 * @param transfersNotAllowedMessage a message to disconnect a client that is trying to join the server due to a server
 *                                   transfer when it is not allowed
 * @since 1.0
 * @see ServerConfiguration
 */
public record JetServerConfiguration(@NonNull String address, int port, int compressionThreshold,
                                     @NonNull NettyTransportSelector transportSelector,
                                     @NonNull Component unsupportedVersionMessage,
                                     @NonNull Component serverListDescription, int maximumPlayers,
                                     @NonNull Set<Key> enabledFeaturePacks, boolean transfersAllowed,
                                     @NonNull Component transfersNotAllowedMessage) implements ServerConfiguration {
    /**
     * Constructs the {@linkplain JetServerConfiguration server configuration}.
     *
     * @param address an address that the sever should bind to
     * @param port a port that the server should bind to
     * @param compressionThreshold a packet length, since which packets should be compressed
     * @param transportSelector a netty transport selector that the server should use for selecting netty native
     *                          transport
     * @param unsupportedVersionMessage a message that should be used during disconnection when a client is trying to
     *                                  join with an unsupported version
     * @param serverListDescription a message that should be used as a description of default server list ping
     * @param maximumPlayers a maximum amount of players, which can be on the server at once
     * @param enabledFeaturePacks a set of keys of feature packs that should be enabled on the server
     * @param transfersAllowed whether clients transferred to the server from another server should be able to join the
     *                         server
     * @param transfersNotAllowedMessage a message to disconnect a client that is trying to join the server due to
     *                                   a server transfer when it is not allowed
     * @since 1.0
     */
    public JetServerConfiguration {
        NullabilityUtil.requireNonNull(address, "address");
        NullabilityUtil.requireNonNull(transportSelector, "transport selector");
        NullabilityUtil.requireNonNull(unsupportedVersionMessage, "unsupported version message");
        NullabilityUtil.requireNonNull(serverListDescription, "server list description");
        NullabilityUtil.requireNonNull(enabledFeaturePacks, "enabled feature packs");
        NullabilityUtil.requireNonNull(transfersAllowed, "transfers not allowed message");
        enabledFeaturePacks = Set.copyOf(enabledFeaturePacks);
    }

    /**
     * Parses {@linkplain UnparsedServerConfiguration an unparsed server configuration} specified for
     * {@linkplain JetMinecraftServer a Minecraft server} specified.
     *
     * @param server the unparsed server configuration
     * @param unparsed the Minecraft server
     * @return a configuration that has been parsed
     * @since 1.0
     */
    public static @NonNull JetServerConfiguration parse(@NonNull JetMinecraftServer server,
                                                        @NonNull UnparsedServerConfiguration unparsed) {
        TagResolver[] tagResolvers = ConfigurationPlaceholder.createTagResolvers(server);
        return new JetServerConfiguration(
                unparsed.address(), unparsed.port(), unparsed.compressionThreshold(), unparsed.transportSelector(),
                deserialize(unparsed.unsupportedVersionMessage(), tagResolvers),
                deserialize(unparsed.serverListDescription(), tagResolvers), unparsed.maximumPlayers(),
                Set.copyOf(unparsed.enabledFeaturePacks()), unparsed.areTransfersAllowed(),
                deserialize(unparsed.transfersNotAllowedMessage(), tagResolvers)
        );
    }

    private static @NonNull Component deserialize(@NonNull String serialized,
                                                  @NonNull TagResolver @NonNull [] tagResolvers) {
        return MiniMessage.miniMessage().deserialize(serialized, tagResolvers);
    }
}