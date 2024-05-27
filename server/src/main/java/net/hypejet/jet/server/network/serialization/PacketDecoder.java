package net.hypejet.jet.server.network.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.buffer.ReadOnlyNetworkBuffer;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacketRegistry;
import net.hypejet.jet.server.buffer.ReadOnlyNetworkBufferImpl;
import net.hypejet.jet.server.player.JetPlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public final class PacketDecoder extends ByteToMessageDecoder {

    private final JetPlayerConnection playerConnection;
    private final ServerBoundPacketRegistry packetRegistry;

    public PacketDecoder(@NonNull JetPlayerConnection playerConnection,
                         @NonNull ServerBoundPacketRegistry packetRegistry) {
        this.playerConnection = playerConnection;
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!this.playerConnection.getChannel().isActive()) return; // The connection was closed

        ReadOnlyNetworkBuffer buffer = new ReadOnlyNetworkBufferImpl(in);

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