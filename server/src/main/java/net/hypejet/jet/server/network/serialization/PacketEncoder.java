package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.network.buffer.NetworkBuffer;
import net.hypejet.jet.server.network.protocol.ServerPacketRegistry;
import net.hypejet.jet.server.player.SocketPlayerConnection;
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

            NetworkBuffer buffer = new NetworkBuffer(buf);
            ServerPacketRegistry.write(buffer, msg, this.playerConnection.getProtocolState());

            int dataLength = buf.readableBytes();
            int compressionThreshold = this.playerConnection.compressionThreshold();

            if (compressionThreshold < 0) {
                NetworkUtil.writeVarInt(out, dataLength);
                out.writeBytes(buf);
                return;
            }

            ByteBuf compressionBuf = Unpooled.buffer();
            NetworkBuffer compressionBuffer = new NetworkBuffer(compressionBuf);

            if (compressionThreshold > dataLength) {
                compressionBuffer.writeVarInt(0);
                compressionBuffer.write(buffer);
            } else {
                compressionBuffer.writeVarInt(dataLength);
                compressionBuffer.writeByteArray(CompressionUtil.compress(buf.array()), false);
            }

            NetworkUtil.writeVarInt(out, compressionBuf.readableBytes());
            out.writeBytes(compressionBuf);
        } catch (Throwable throwable) {
            this.playerConnection.close(); // Close the connection to avoid more issues
            LOGGER.error("An error occurred while encoding a packet", throwable);
        }
    }
}