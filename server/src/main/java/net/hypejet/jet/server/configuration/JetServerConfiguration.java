package net.hypejet.jet.server.configuration;

import net.hypejet.jet.configuration.ServerConfiguration;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.unparsed.UnparsedServerConfiguration;
import net.hypejet.jet.server.network.netty.transport.NettyTransportSelector;
import net.hypejet.jet.server.world.handler.ChunkBatchHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

import java.util.Objects;

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
 * @param transfersAllowed whether clients transferred to the server from another server should be able to join the
 *                         server
 * @param transfersNotAllowedMessage a message to disconnect a client that is trying to join the server due to a server
 *                                   transfer when it is not allowed
 * @param maximumViewDistance a maximum chunk view distance that a player can have
 * @param simulationDistance a chunk distance within the server and clients should process entities
 * @param hardcore whether the server should be in hardcore more
 * @param reducedDebugInfo whether information displayed on the debug screen of clients should be reduced
 * @param enforceSecureProfile whether only clients with signed public key from Mojang should be able to join
 *                             the server
 * @param showUnlockedRecipesOnly whether players can only see recipes that they unlocked
 * @param tickDuration a duration that each game logic loop cycle of the server should have, in milliseconds
 * @since 1.0
 * @see ServerConfiguration
 */
public record JetServerConfiguration(
        @NonNull String address, int port, int compressionThreshold, @NonNull NettyTransportSelector transportSelector,
        @NonNull Component unsupportedVersionMessage, @NonNull Component serverListDescription,
        int maximumPlayers, boolean transfersAllowed,@NonNull Component transfersNotAllowedMessage,
        @IntRange(from = ChunkBatchHandler.MINIMUM_VIEW_DISTANCE) byte maximumViewDistance, byte simulationDistance,
        boolean hardcore, boolean reducedDebugInfo, boolean enforceSecureProfile, boolean showUnlockedRecipesOnly,
        long tickDuration
) implements ServerConfiguration {
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
     * @param transfersAllowed whether clients transferred to the server from another server should be able to join the
     *                         server
     * @param transfersNotAllowedMessage a message to disconnect a client that is trying to join the server due to
     *                                   a server transfer when it is not allowed
     * @param maximumViewDistance a maximum chunk view distance that a player can have
     * @param simulationDistance a chunk distance within the server and clients should process entities
     * @param hardcore whether the server should be in hardcore more
     * @param reducedDebugInfo whether information displayed on the debug screen of clients should be reduced
     * @param enforceSecureProfile whether only clients with signed public key from Mojang should be able to join
     *                             the server
     * @param showUnlockedRecipesOnly whether players can only see recipes that they unlocked
     * @param tickDuration a duration that each game logic loop cycle of the server should have, in milliseconds
     * @since 1.0
     */
    public JetServerConfiguration {
        Objects.requireNonNull(address, "address");
        Objects.requireNonNull(transportSelector, "transport selector");
        Objects.requireNonNull(unsupportedVersionMessage, "unsupported version message");
        Objects.requireNonNull(serverListDescription, "server list description");
        Objects.requireNonNull(transfersAllowed, "transfers not allowed message");

        byte minimumViewDistance = ChunkBatchHandler.MINIMUM_VIEW_DISTANCE;
        if (maximumViewDistance < minimumViewDistance) {
            throw new IllegalArgumentException(String.format(
                    "The maximum view distance value (%d) cannot be lower than the minimum view distance value (%d)",
                    maximumViewDistance, minimumViewDistance
            ));
        }
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
                unparsed.areTransfersAllowed(), deserialize(unparsed.transfersNotAllowedMessage(), tagResolvers),
                unparsed.maximumViewDistance(), unparsed.simulationDistance(), unparsed.isHardcore(),
                unparsed.reducedDebugInfo(), unparsed.enforceSecureProfile(), unparsed.unlockedRecipesOnly(),
                unparsed.tickDuration()
        );
    }

    private static @NonNull Component deserialize(@NonNull String serialized,
                                                  @NonNull TagResolver @NonNull [] tagResolvers) {
        return MiniMessage.miniMessage().deserialize(serialized, tagResolvers);
    }
}