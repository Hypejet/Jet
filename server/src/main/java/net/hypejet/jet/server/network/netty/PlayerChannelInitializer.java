package net.hypejet.jet.server.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import net.hypejet.jet.protocol.connection.PlayerConnection;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.netty.decoder.PacketDecoder;
import net.hypejet.jet.server.network.netty.decoder.PacketLengthDecoder;
import net.hypejet.jet.server.network.netty.encoder.PacketEncoder;
import net.hypejet.jet.server.network.netty.encoder.PacketLengthEncoder;
import net.hypejet.jet.server.network.netty.reader.PacketReader;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketRegistry;
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

    private final JetMinecraftServer server;

    private final ClientPacketRegistry clientPacketRegistry = new ClientPacketRegistry();
    private final ServerPacketRegistry serverPacketRegistry = new ServerPacketRegistry();

    /**
     * Constructs a {@link PlayerChannelInitializer player channel initializer}.
     *
     * @param server a {@linkplain JetMinecraftServer minecraft server}, which provides player connections
     * @since 1.0
     */
    public PlayerChannelInitializer(@NonNull JetMinecraftServer server) {
        this.server = NullabilityUtil.requireNonNull(server, "server");
    }

    @Override
    protected void initChannel(@NonNull SocketChannel ch) {
        SocketPlayerConnection connection = new SocketPlayerConnection(ch, this.server);
        ch.pipeline()
                .addFirst(PACKET_ENCODER, new PacketEncoder(connection, this.serverPacketRegistry))
                .addFirst(PACKET_DECODER, new PacketDecoder(connection, this.clientPacketRegistry))
                .addBefore(PACKET_DECODER, PACKET_LENGTH_DECODER, new PacketLengthDecoder(connection))
                .addBefore(PACKET_ENCODER, PACKET_LENGTH_ENCODER, new PacketLengthEncoder(connection))
                .addAfter(PACKET_DECODER, PACKET_READER, new PacketReader(connection));
        ch.closeFuture().addListener(future -> connection.handleDisconnection());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("An error occurred while initializing a socket channel", cause);
    }
}