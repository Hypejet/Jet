package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.packet.server.ServerPacket;
import net.hypejet.jet.server.buffer.NetworkBufferImpl;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.zip.Deflater;

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

            NetworkBuffer buffer = new NetworkBufferImpl(buf);
            buffer.writeVarInt(msg.getPacketId()); // Packet ID
            msg.write(buffer);

            int threshold = this.playerConnection.compressionThreshold();
            int dataLength = buf.readableBytes();

            if (threshold < 0) {
                NetworkUtil.writeVarInt(out, dataLength);
                out.writeBytes(buf);
                return;
            }

            if (dataLength < threshold) {
                NetworkUtil.writeVarInt(out, dataLength + 1); // TODO: Document why it is data length + 1
                NetworkUtil.writeVarInt(out, 0);
                out.writeBytes(buf);
                return;
            }

            ByteBuf compressedBuf = Unpooled.buffer();
            NetworkBuffer compressedBuffer = new NetworkBufferImpl(compressedBuf);

            byte[] output = new byte[dataLength];

            Deflater deflater = new Deflater();
            deflater.setInput(buf.array());
            deflater.finish();
            deflater.deflate(output);
            deflater.end();

            compressedBuffer.writeVarInt(dataLength);
            compressedBuffer.writeByteArray(output);

            NetworkUtil.writeVarInt(out, compressedBuf.readableBytes());
            out.writeBytes(compressedBuf);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            this.playerConnection.close(); // Close the connection to avoid more issues
            LOGGER.error("An error occurred while encoding a packet", throwable);
        }
    }
}