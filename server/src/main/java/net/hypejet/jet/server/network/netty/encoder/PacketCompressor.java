package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.util.CompressionUtil;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@linkplain MessageToByteEncoder message-to-byte encoder}, which compresses serialized
 * {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packets}.
 *
 * @since 1.0
 * @author Codsestech
 * @see net.hypejet.jet.protocol.packet.server.ServerPacket
 * @see MessageToByteEncoder
 */
public final class PacketCompressor extends MessageToByteEncoder<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketCompressor.class);

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain PacketCompressor packet compressor}.
     *
     * @param connection a connection that compression should be handled for
     * @since 1.0
     */
    public PacketCompressor(@NonNull SocketPlayerConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        try {
            int compressionThreshold = this.connection.compressionThreshold();
            int dataLength = msg.readableBytes();

            if (compressionThreshold > dataLength) {
                VarIntNetworkCodec.instance().write(out, 0);
                out.writeBytes(msg);
            } else {
                VarIntNetworkCodec.instance().write(out, dataLength);
                out.writeBytes(CompressionUtil.compress(NetworkUtil.readRemainingBytes(msg)));
            }
        } catch (Throwable throwable) {
            LOGGER.error("An error occurred while encoding a packet", throwable);
            ctx.channel().close().sync();
        }
    }
}