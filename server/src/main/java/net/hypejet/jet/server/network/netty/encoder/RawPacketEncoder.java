package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.server.network.SocketPlayerConnection;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.RawPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * Represents {@linkplain MessageToByteEncoder a message-to-byte encoder}, which encodes outgoing packets into
 * {@linkplain RawPacket raw packets}.
 *
 * @since 1.0
 * @see RawPacket
 * @see MessageToByteEncoder
 */
public final class RawPacketEncoder extends MessageToByteEncoder<RawPacket> {

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain RawPacketEncoder packet encoder}.
     *
     * @param connection a connection that the encoding should be handled for
     * @since 1.0
     */
    public RawPacketEncoder(@NonNull SocketPlayerConnection connection) {
        this.connection = Objects.requireNonNull(connection, "connection");
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RawPacket msg, ByteBuf out) {
        try {
            VarIntNetworkCodec.INSTANCE.write(out, this.connection.server().registryManager(), msg.identifier());
            out.writeBytes(msg.body().array());
        } catch (Throwable throwable) {
            this.connection.uncaughtException(Thread.currentThread(), throwable);
        }
    }
}