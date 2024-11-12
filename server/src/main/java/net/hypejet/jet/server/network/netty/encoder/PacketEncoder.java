package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain MessageToByteEncoder message-to-byte encoder}, which encodes {@linkplain ServerPacket
 * server packets}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 * @see MessageToByteEncoder
 */
public final class PacketEncoder extends MessageToByteEncoder<ServerPacket> {

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain PacketEncoder packet encoder}.
     *
     * @param connection a connection that encoding should be handled for
     * @since 1.0
     */
    public PacketEncoder(@NonNull SocketPlayerConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerPacket msg, ByteBuf out) {
        try {
            PacketCodec<? extends ServerPacket> codec = ServerPacketRegistry.codec(msg.getClass());
            if (codec == null) throw new IllegalArgumentException("Could not find a packet codec for: " + msg);
            write(codec, out, msg); // Write the packet with java generics
        } catch (Throwable throwable) {
            this.connection.uncaughtException(Thread.currentThread(), throwable);
        }
    }

    private static <P extends ServerPacket> void write(@NonNull PacketCodec<P> codec, @NonNull ByteBuf buf,
                                                       @NonNull ServerPacket packet) {
        VarIntNetworkCodec.instance().write(buf, codec.getPacketId());
        codec.write(buf, codec.getPacketClass().cast(packet));
    }
}