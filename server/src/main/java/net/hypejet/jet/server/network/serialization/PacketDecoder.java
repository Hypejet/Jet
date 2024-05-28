package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.buffer.NetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import net.hypejet.jet.server.buffer.NetworkBufferImpl;
import net.hypejet.jet.server.player.SocketPlayerConnection;
import net.hypejet.jet.server.protocol.ServerBoundPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents a {@link ByteToMessageDecoder byte-to-message decoder}, which decodes Minecraft packets.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerBoundPacket
 */
public final class PacketDecoder extends ByteToMessageDecoder {

    private final SocketPlayerConnection playerConnection;
    private final ServerBoundPacketRegistry packetRegistry;

    /**
     * Constructs a {@link PacketDecoder}.
     *
     * @param playerConnection a player connection to decode packets for
     * @param packetRegistry a registry of readable packets to read packets from
     * @since 1.0
     */
    public PacketDecoder(@NonNull SocketPlayerConnection playerConnection,
                         @NonNull ServerBoundPacketRegistry packetRegistry) {
        this.playerConnection = playerConnection;
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        NetworkBuffer buffer = new NetworkBufferImpl(in);

        buffer.readVarInt(); // TODO: Check if it's necessary anywhere (the packet size)
        int packetId = buffer.readVarInt();

        ProtocolState protocolState = this.playerConnection.getProtocolState();
        ServerBoundPacket packet = this.packetRegistry.read(packetId, protocolState, buffer);

        if (packet == null) throw packetReaderNotFound(packetId, protocolState);

        out.add(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        this.playerConnection.close(); // Close the connection to avoid more issues
    }

    private static @NonNull Exception packetReaderNotFound(int packetId, @NonNull ProtocolState protocolState) {
        return new IllegalStateException("Could not find a reader of a packet with id of "+ packetId + " in protocol" +
                " state " + protocolState);
    }
}