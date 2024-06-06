package net.hypejet.jet.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.transport.NettyTransportType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents something that manages player connections using netty.
 *
 * @since 1.0
 * @author Codestech
 */
public final class NetworkManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkManager.class);

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    private final Channel channel;

    /**
     * Constructs the {@linkplain NetworkManager network manager}.
     *
     * @param server the server accepting connections
     * @since 1.0
     */
    public NetworkManager(@NonNull JetMinecraftServer server) {
        JetServerConfiguration configuration = server.configuration();
        NettyTransportType transport = configuration.transportSelector().getTransportType();

        if (!transport.isAvailable()) {
            NettyTransportType oldTransport = transport;
            transport = NettyTransportType.select();
            LOGGER.warn("The netty transport specified - {} - is not available, using {}...", oldTransport, transport);
        }

        this.bossGroup = transport.createEventLoop();
        this.workerGroup = transport.createEventLoop();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(this.bossGroup, this.workerGroup)
                .channel(transport.getSocketChannel())
                .childHandler(new PlayerChannelInitializer(server));

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
        try {
            this.channel.close().await();
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}