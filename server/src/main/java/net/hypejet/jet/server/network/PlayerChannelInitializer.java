package net.hypejet.jet.server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.server.network.serialization.PacketDecoder;
import net.hypejet.jet.server.network.serialization.PacketEncoder;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import net.hypejet.jet.server.protocol.ClientPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@link ChannelInitializer channel initializer}, which initializes
 * {@link io.netty.channel.ChannelHandler channel handlers} and
 * a {@link net.hypejet.jet.player.PlayerConnection player connection}.
 *
 * @since 1.0
 * @author Codestech
 *
 * @see ChannelInitializer
 * @see io.netty.channel.ChannelHandler
 * @see net.hypejet.jet.player.PlayerConnection
 */
public final class PlayerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerChannelInitializer.class);

    private final ClientPacketRegistry clientPacketRegistry;

    /**
     * Constructs a {@link PlayerChannelInitializer player channel initializer}.
     *
     * @param clientPacketRegistry a client packet registry to read packets from
     * @since 1.0
     */
    public PlayerChannelInitializer(@NonNull ClientPacketRegistry clientPacketRegistry) {
        this.clientPacketRegistry = clientPacketRegistry;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        SocketPlayerConnection playerConnection = new SocketPlayerConnection(ch);
        ch.pipeline()
                .addFirst(new PacketEncoder(playerConnection))
                .addFirst(new PacketReader(playerConnection))
                .addFirst(new PacketDecoder(playerConnection, this.clientPacketRegistry));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("An error occurred while initializing a socket channel", cause);
    }
}