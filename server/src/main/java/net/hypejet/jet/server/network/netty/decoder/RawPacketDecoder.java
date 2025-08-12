package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.RawPacket;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents {@linkplain ByteToMessageDecoder a byte-to-message decoder}, which decodes incoming packets into
 * {@linkplain RawPacket raw packets}.
 *
 * @since 1.0
 * @see RawPacket
 * @see ByteToMessageDecoder
 */
public final class RawPacketDecoder extends ByteToMessageDecoder {

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain RawPacketDecoder packet decoder}.
     *
     * @param connection a connection that the decoding should be handled for
     * @since 1.0
     */
    public RawPacketDecoder(@NonNull SocketPlayerConnection connection) {
        this.connection = Objects.requireNonNull(connection, "connection");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) return; // The connection has been closed

        int identifier = VarIntNetworkCodec.INSTANCE.read(in);
        byte[] body = NetworkUtil.readRemainingBytes(in);

        out.add(new RawPacket(identifier, body));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.connection.uncaughtException(Thread.currentThread(), cause);
    }
}