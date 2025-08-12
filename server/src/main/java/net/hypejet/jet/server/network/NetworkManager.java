package net.hypejet.jet.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.netty.ConnectionInitializer;
import net.hypejet.jet.server.network.netty.transport.NettyTransportType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents something that manages {@linkplain net.hypejet.jet.network.PlayerConnection player connections}.
 *
 * @since 1.0
 */
public final class NetworkManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkManager.class);

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    private final Channel channel;

    /**
     * Constructs the {@linkplain NetworkManager network manager}.
     *
     * @param server the server that should accept connections
     * @since 1.0
     */
    public NetworkManager(@NonNull JetMinecraftServer server) {
        Objects.requireNonNull(server, "server");

        JetServerConfiguration configuration = server.configuration();
        NettyTransportType transport = configuration.transportSelector().getTransportType();

        if (!transport.isAvailable()) {
            NettyTransportType oldTransport = transport;
            transport = NettyTransportType.select();
            LOGGER.warn("The netty transport specified - {} - is not available, falling back to {}...",
                    oldTransport, transport);
        }

        this.bossGroup = transport.createEventLoop();
        this.workerGroup = transport.createEventLoop();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(this.bossGroup, this.workerGroup)
                .channel(transport.getSocketChannel())
                .childHandler(new ConnectionInitializer(server));

        String address = configuration.address();
        int port = configuration.port();

        this.channel = bootstrap.bind(address, port).awaitUninterruptibly().channel();
        LOGGER.info("Listening on {}:{}", address, port);
    }

    /**
     * Shuts down the {@linkplain NetworkManager network manager}.
     *
     * @since 1.0
     */
    public void shutdown() {
        this.channel.close().awaitUninterruptibly();
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }
}