package net.hypejet.jet.server.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.network.PlayerConnection;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents {@link ChannelInitializer a channel initializer}, which initializes {@linkplain PlayerConnection player
 * connections}.
 *
 * @since 1.0
 * @see ChannelInitializer
 * @see PlayerConnection
 */
public final class ConnectionInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionInitializer.class);

    private final JetMinecraftServer server;

    /**
     * Constructs the {@link ConnectionInitializer connection initializer}.
     *
     * @param server a minecraft server, which should own the player connections
     * @since 1.0
     */
    public ConnectionInitializer(@NonNull JetMinecraftServer server) {
        this.server = NullabilityUtil.requireNonNull(server, "server");
    }

    @Override
    protected void initChannel(@NonNull SocketChannel ch) {
        SocketPlayerConnection connection = new SocketPlayerConnection(ch, this.server);
        ch.closeFuture().addListener(future -> connection.handleDisconnection());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("An error occurred while initializing a socket channel", cause);
    }
}