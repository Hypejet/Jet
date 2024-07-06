package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import net.hypejet.jet.server.util.CompressionUtil;
import net.hypejet.jet.server.util.NetworkUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a {@link ByteToMessageDecoder byte-to-message decoder}, which decodes Minecraft packets.
 *
 * @since 1.0
 * @author Codestech
 * @see ClientPacket
 */
public final class PacketDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDecoder.class);
    private static final int MAX_PACKET_LENGTH = 8_388_608;

    private final SocketPlayerConnection playerConnection;

    /**
     * Constructs a {@link PacketDecoder}.
     *
     * @param playerConnection a player connection to decode packets for
     * @since 1.0
     */
    public PacketDecoder(@NonNull SocketPlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        int compressionThreshold = this.playerConnection.compressionThreshold();
        int packetLength = NetworkUtil.readVarInt(in);

        if (packetLength >= MAX_PACKET_LENGTH || packetLength > in.readableBytes()) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf framedInput = Unpooled.buffer(packetLength);
        in.readBytes(framedInput, packetLength);

        if (compressionThreshold < 0) {
            out.add(this.readPacket(framedInput));
            return;
        }

        int dataLength = NetworkUtil.readVarInt(framedInput);

        if (dataLength == 0) {
            out.add(this.readPacket(framedInput));
            return;
        }

        byte[] compressed = NetworkUtil.readRemainingBytes(framedInput);
        ByteBuf uncompressedBuf = Unpooled.wrappedBuffer(CompressionUtil.decompress(compressed));

        out.add(this.readPacket(uncompressedBuf));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.playerConnection.close(); // Close the connection to avoid more issues
        LOGGER.error("An error occurred while decoding a packet", cause);
    }

    private @NonNull ClientPacket readPacket(@NonNull ByteBuf buf) {
        int packetId = NetworkUtil.readVarInt(buf);

        ProtocolState protocolState = this.playerConnection.getProtocolState();
        ClientPacket packet = ClientPacketRegistry.read(packetId, protocolState, buf);

        if (packet == null) throw packetReaderNotFound(packetId, protocolState);

        return packet;
    }

    private static @NonNull RuntimeException packetReaderNotFound(int packetId, @NonNull ProtocolState protocolState) {
        return new IllegalStateException("Could not find a reader of a packet with id of "+ packetId + " in protocol" +
                " state " + protocolState);
    }
}