package net.hypejet.jet.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hypejet.jet.protocol.ProtocolState;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacket;
import net.hypejet.jet.protocol.packet.serverbound.ServerBoundPacketRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public final class PacketDecoder extends ByteToMessageDecoder {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    private final JetPlayerConnection playerConnection;
    private final ServerBoundPacketRegistry packetRegistry;

    public PacketDecoder(@NonNull JetPlayerConnection playerConnection,
                         @NonNull ServerBoundPacketRegistry packetRegistry) {
        this.playerConnection = playerConnection;
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int packetSize = readVarInt(in);
        int packetId = readVarInt(in);

        ProtocolState protocolState = this.playerConnection.getProtocolState();
        ServerBoundPacket packet = this.packetRegistry.read(packetId, protocolState, in);

        if (packet == null) throw packetReaderNotFound(packetId, protocolState);

        out.add(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    public static int readVarInt(ByteBuf in) {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = in.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public static String readString(ByteBuf in) {
        int length = readVarInt(in);
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = in.readByte();
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static UUID readUUID(ByteBuf in) {
        return new UUID(in.readLong(), in.readLong());
    }

    private static @NonNull Exception packetReaderNotFound(int packetId, @NonNull ProtocolState protocolState) {
        return new IllegalStateException("Could not find a reader of a packet with id of "+ packetId + " in protocol" +
                " state " + protocolState);
    }
}