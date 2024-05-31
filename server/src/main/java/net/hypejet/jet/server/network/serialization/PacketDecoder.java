package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.buffer.NetworkBufferImpl;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import net.hypejet.jet.server.protocol.ClientPacketRegistry;
import net.hypejet.jet.server.util.CompressionUtil;
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

    private final SocketPlayerConnection playerConnection;
    private final ClientPacketRegistry packetRegistry;

    /**
     * Constructs a {@link PacketDecoder}.
     *
     * @param playerConnection a player connection to decode packets for
     * @param packetRegistry a registry of readable packets to read packets from
     * @since 1.0
     */
    public PacketDecoder(@NonNull SocketPlayerConnection playerConnection,
                         @NonNull ClientPacketRegistry packetRegistry) {
        this.playerConnection = playerConnection;
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        NetworkBuffer buffer = new NetworkBufferImpl(in);
        int compressionThreshold = this.playerConnection.compressionThreshold();

        buffer.readVarInt(); // TODO: Check if the packet length is necessary anywhere

        if (compressionThreshold < 0) {
            out.add(this.readPacket(buffer));
            return;
        }

        int dataLength = buffer.readVarInt();

        if (dataLength == 0) {
            out.add(this.readPacket(buffer));
            return;
        }

        byte[] compressed = buffer.readByteArray(false);

        ByteBuf uncompressedBuf = Unpooled.wrappedBuffer(CompressionUtil.decompress(compressed));
        NetworkBuffer uncompressedBuffer = new NetworkBufferImpl(uncompressedBuf);

        out.add(this.readPacket(uncompressedBuffer));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.playerConnection.close(); // Close the connection to avoid more issues
        LOGGER.error("An error occurred while decoding a packet", cause);
    }

    private @NonNull ClientPacket readPacket(@NonNull NetworkBuffer buffer) {
        int packetId = buffer.readVarInt();

        ProtocolState protocolState = this.playerConnection.getProtocolState();
        ClientPacket packet = this.packetRegistry.read(packetId, protocolState, buffer);

        if (packet == null) throw packetReaderNotFound(packetId, protocolState);

        return packet;
    }

    private static @NonNull RuntimeException packetReaderNotFound(int packetId, @NonNull ProtocolState protocolState) {
        return new IllegalStateException("Could not find a reader of a packet with id of "+ packetId + " in protocol" +
                " state " + protocolState);
    }
}