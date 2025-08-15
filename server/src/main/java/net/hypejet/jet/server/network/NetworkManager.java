package net.hypejet.jet.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.configuration.JetServerConfiguration;
import net.hypejet.jet.server.network.netty.transport.NettyTransportType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Something managing {@linkplain PlayerConnection player connections}.
 *
 * @since 1.0
 * @see PlayerConnection
 */
public final class NetworkManager extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkManager.class);

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    private final JetMinecraftServer server;
    private final ServerBootstrap bootstrap;

    private final Set<SocketPlayerConnection> connections = ConcurrentHashMap.newKeySet();
    private final Set<SocketPlayerConnection> unmodifiableConnections = Collections.unmodifiableSet(this.connections);

    private Channel channel;

    /**
     * Constructs the {@linkplain NetworkManager network manager}.
     *
     * @param server a server that the network manager is being constructed for
     * @since 1.0
     */
    public NetworkManager(@NonNull JetMinecraftServer server) {
        this.server = Objects.requireNonNull(server, "server");

        NettyTransportType transport = server.configuration().transportSelector().getTransportType();
        if (!transport.isAvailable()) {
            NettyTransportType oldTransport = transport;
            transport = NettyTransportType.select();
            LOGGER.warn(
                    "The netty transport specified - {} - is not available, falling back to {}...",
                    oldTransport, transport
            );
        }

        this.bossGroup = transport.createEventLoop();
        this.workerGroup = transport.createEventLoop();

        this.bootstrap = new ServerBootstrap()
                .group(this.bossGroup, this.workerGroup)
                .channel(transport.getSocketChannelClass())
                .childHandler(this);
    }

    /**
     * Creates and binds a {@linkplain Channel channel} for this {@linkplain NetworkManager network manager}.
     *
     * @since 1.0
     * @see Channel
     */
    public void bind() {
        if (this.channel != null)
            throw new IllegalStateException("The network manager has already been started");

        JetServerConfiguration configuration = this.server.configuration();
        String address = configuration.address();
        int port = configuration.port();

        this.channel = this.bootstrap.bind(address, port).awaitUninterruptibly().channel();
        LOGGER.info("Listening on {}", this.channel.localAddress());
    }

    /**
     * Shuts down the {@linkplain NetworkManager network manager}.
     *
     * @since 1.0
     */
    public void shutdown() {
        if (this.channel == null)
            throw new IllegalStateException("The network manager is not started");

        this.channel.close().awaitUninterruptibly();
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();

        this.channel = null;
    }

    /**
     * Gets an unmodifiable view of a {@linkplain Set set} of currently
     * {@linkplain SocketPlayerConnection#isActive() active} {@linkplain SocketPlayerConnection player connections}
     * associated with this {@linkplain NetworkManager network manager}.
     *
     * @return the player-connection set
     * @since 1.0
     */
    public @NonNull Set<SocketPlayerConnection> connections() {
        return this.unmodifiableConnections;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        SocketPlayerConnection connection = new SocketPlayerConnection(ch, this.server);
        ch.closeFuture().addListener(future -> {
            this.connections.remove(connection);
            connection.handleDisconnection();
        });
        this.connections.add(connection);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("An error occurred while initializing a socket channel", cause);
    }
}