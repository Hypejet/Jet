package net.hypejet.jet.server.network.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerPacket msg, ByteBuf out) throws Exception {
        try {
            PacketCodec<? extends ServerPacket> codec = ServerPacketRegistry.codec(msg.getClass());
            if (codec == null) throw new IllegalArgumentException("Could not find a packet codec for: " + msg);
            write(codec, out, msg); // Write the packet with java generics
        } catch (Throwable throwable) {
            LOGGER.error("An error occurred while encoding a packet", throwable);
            ctx.channel().close().sync();
        }
    }

    private static <P extends ServerPacket> void write(@NonNull PacketCodec<P> codec, @NonNull ByteBuf buf,
                                                       @NonNull ServerPacket packet) {
        codec.write(buf, codec.getPacketClass().cast(packet));
    }
}