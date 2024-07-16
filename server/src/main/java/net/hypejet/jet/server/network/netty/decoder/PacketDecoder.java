package net.hypejet.jet.server.network.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.client.ClientPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.connection.SocketPlayerConnection;
import net.hypejet.jet.server.network.protocol.packet.client.ClientPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a {@linkplain ByteToMessageDecoder byte-to-message decoder}, which decodes
 * {@linkplain net.hypejet.jet.protocol.packet.server.ServerPacket server packets} and their identifiers.
 *
 * @since 1.0
 * @author Codestech
 * @see net.hypejet.jet.protocol.packet.server.ServerPacket
 * @see ByteToMessageDecoder
 */
public final class PacketDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDecoder.class);

    private final SocketPlayerConnection connection;

    /**
     * Constructs the {@linkplain PacketDecoder packet decoder}.
     *
     * @param connection a connection that the decoding should be handled for
     * @since 1.0
     */
    public PacketDecoder(@NonNull SocketPlayerConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) return; // The connection was closed
        int packetId = VarIntNetworkCodec.instance().read(in);

        ProtocolState protocolState = this.connection.getProtocolState();
        ClientPacket packet = ClientPacketRegistry.read(packetId, protocolState, in);

        if (packet == null) throw packetReaderNotFound(packetId, protocolState);

        out.add(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("An error occurred while decoding a packet", cause);
        ctx.channel().close().sync();
    }

    private static @NonNull RuntimeException packetReaderNotFound(int packetId, @NonNull ProtocolState protocolState) {
        return new IllegalStateException("Could not find a reader of a packet with id of "
                + packetId + " in protocol state " + protocolState);
    }
}