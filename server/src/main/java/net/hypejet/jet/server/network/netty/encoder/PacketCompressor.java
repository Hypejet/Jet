package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.registry.JetRegistryManager;
import net.hypejet.jet.server.util.CompressionUtil;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain MessageToByteEncoder a message-to-byte encoder}, which compresses outgoing packets.
 *
 * @since 1.0
 * @see MessageToByteEncoder
 */
public final class PacketCompressor extends MessageToByteEncoder<ByteBuf> {

    private final int compressionThreshold;
    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain PacketCompressor packet compressor}.
     *
     * @param connection a connection that compression should be handled for
     * @param compressionThreshold a threshold of the compression
     * @since 1.0
     */
    public PacketCompressor(@NonNull SocketPlayerConnection connection, int compressionThreshold) {
        this.connection = Objects.requireNonNull(connection, "connection");
        this.compressionThreshold = compressionThreshold;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        try {
            int dataLength = msg.readableBytes();
            JetRegistryManager registryManager = this.connection.server().registryManager();

            if (this.compressionThreshold > dataLength) {
                VarIntNetworkCodec.INSTANCE.write(out, registryManager, 0); // 0 indicates uncompressed
                out.writeBytes(msg);
            } else {
                VarIntNetworkCodec.INSTANCE.write(out, registryManager, dataLength);
                out.writeBytes(CompressionUtil.compress(NetworkUtil.readRemainingBytes(msg)));
            }
        } catch (Throwable throwable) {
            this.connection.uncaughtException(Thread.currentThread(), throwable);
        }
    }
}