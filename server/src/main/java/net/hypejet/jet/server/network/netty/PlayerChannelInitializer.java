package net.hypejet.jet.server.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.netty.decoder.PacketDecoder;
import net.hypejet.jet.server.network.netty.decoder.PacketLengthDecoder;
import net.hypejet.jet.server.network.netty.encoder.PacketEncoder;
import net.hypejet.jet.server.network.netty.encoder.PacketLengthEncoder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_DECODER;
import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_ENCODER;
import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_LENGTH_DECODER;
import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_LENGTH_ENCODER;
import static net.hypejet.jet.server.network.netty.ChannelHandlers.PACKET_READER;

/**
 * Represents a {@link ChannelInitializer channel initializer}, which initializes
 * {@link io.netty.channel.ChannelHandler channel handlers} and
 * a {@link PlayerConnection player connection}.
 *
 * @since 1.0
 * @author Codestech
 *
 * @see ChannelInitializer
 * @see io.netty.channel.ChannelHandler
 * @see PlayerConnection
 */
public final class PlayerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerChannelInitializer.class);

    private final JetMinecraftServer minecraftServer;

    /**
     * Constructs a {@link PlayerChannelInitializer player channel initializer}.
     *
     * @param minecraftServer a {@linkplain JetMinecraftServer minecraft server}, which provides player connections
     * @since 1.0
     */
    public PlayerChannelInitializer(@NonNull JetMinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        SocketPlayerConnection connection = new SocketPlayerConnection(ch, this.minecraftServer);

        ch.pipeline()
                .addFirst(PACKET_ENCODER, new PacketEncoder())
                .addFirst(PACKET_DECODER, new PacketDecoder(connection))
                .addBefore(PACKET_DECODER, PACKET_LENGTH_DECODER, new PacketLengthDecoder())
                .addBefore(PACKET_ENCODER, PACKET_LENGTH_ENCODER, new PacketLengthEncoder())
                .addAfter(PACKET_DECODER, PACKET_READER, new PacketReader(connection));

        ch.closeFuture().addListener(future -> connection.getSession()
                .sessionHandler()
                .onConnectionClose(future.cause()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("An error occurred while initializing a socket channel", cause);
    }
}