package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain MessageToByteEncoder message-to-byte encoder}, which writes a length of a serialized
 * and compressed {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerPacket
 * @see MessageToByteEncoder
 */
public final class PacketLengthEncoder extends MessageToByteEncoder<ByteBuf> {

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain PacketLengthEncoder packet length encoder}.
     *
     * @param connection a connection that packet length encoding should be handled for
     * @since 1.0
     */
    public PacketLengthEncoder(@NonNull SocketPlayerConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        try {
            VarIntNetworkCodec.instance().write(out, msg.readableBytes());
            out.writeBytes(msg);
        } catch (Throwable throwable) {
            this.connection.uncaughtException(Thread.currentThread(), throwable);
        }
    }
}