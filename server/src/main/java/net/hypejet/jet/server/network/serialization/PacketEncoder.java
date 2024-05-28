package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.packet.clientbound.ClientBoundPacket;
import net.hypejet.jet.server.buffer.NetworkBufferImpl;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a {@link MessageToByteEncoder message-to-byte encoder}, which encodes Minecraft packets.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientBoundPacket
 */
public final class PacketEncoder extends MessageToByteEncoder<ClientBoundPacket> {

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
    protected void encode(ChannelHandlerContext ctx, ClientBoundPacket msg, ByteBuf out) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        try {
            ByteBuf buf = Unpooled.buffer();

            NetworkBuffer buffer = new NetworkBufferImpl(buf);
            buffer.writeVarInt(msg.getPacketId());
            msg.write(buffer);

            NetworkUtil.writeVarLong(out, buf.readableBytes());
            out.writeBytes(buf);
        } catch (Throwable throwable) {
            this.playerConnection.close(); // Close the connection to avoid more issues
            LOGGER.error("An error occurred while encoding a packet", throwable);
        }
    }
}