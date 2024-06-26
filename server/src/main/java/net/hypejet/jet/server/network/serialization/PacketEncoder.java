package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketRegistry;
import net.hypejet.jet.server.util.CompressionUtil;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@link MessageToByteEncoder message-to-byte encoder}, which encodes Minecraft packets.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerPacket
 */
public final class PacketEncoder extends MessageToByteEncoder<ServerPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketEncoder.class);

    private final SocketPlayerConnection playerConnection;

    /**
     * Constructs a {@link PacketEncoder packet encoder}.
     *
     * @param playerConnection a player connection, which is closed when an error occurs
     * @since 1.0
     */
    public PacketEncoder(@NonNull SocketPlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerPacket msg, ByteBuf out) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        try {
            ByteBuf buf = Unpooled.buffer();
            ServerPacketRegistry.write(buf, msg);

            int dataLength = buf.readableBytes();
            int compressionThreshold = this.playerConnection.compressionThreshold();

            if (compressionThreshold < 0) {
                NetworkUtil.writeVarInt(out, dataLength);
                out.writeBytes(buf);
                out.release();
                return;
            }

            ByteBuf compressionBuf = Unpooled.buffer();

            if (compressionThreshold > dataLength) {
                NetworkUtil.writeVarInt(compressionBuf, 0);
                compressionBuf.writeBytes(buf);
            } else {
                NetworkUtil.writeVarInt(compressionBuf, dataLength);
                compressionBuf.writeBytes(CompressionUtil.compress(buf.array()));
            }

            buf.release();

            NetworkUtil.writeVarInt(out, compressionBuf.readableBytes());
            out.writeBytes(compressionBuf);

            compressionBuf.release();
            out.release();
        } catch (Throwable throwable) {
            this.playerConnection.close(); // Close the connection to avoid more issues
            LOGGER.error("An error occurred while encoding a packet", throwable);
        }
    }
}