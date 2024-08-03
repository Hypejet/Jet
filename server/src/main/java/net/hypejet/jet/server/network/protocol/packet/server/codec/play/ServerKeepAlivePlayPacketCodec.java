package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerKeepAlivePlayPacket;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes the {@linkplain ServerKeepAlivePlayPacket
 * keep alive play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerKeepAlivePlayPacket
 * @see PacketCodec
 */
public final class ServerKeepAlivePlayPacketCodec extends PacketCodec<ServerKeepAlivePlayPacket> {
    /**
     * Constructs the {@linkplain ServerKeepAlivePlayPacketCodec keep alive play packet codec}.
     *
     * @since 1.0
     */
    public ServerKeepAlivePlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_KEEP_ALIVE, ServerKeepAlivePlayPacket.class);
    }

    @Override
    public @NonNull ServerKeepAlivePlayPacket read(@NonNull ByteBuf buf) {
        return new ServerKeepAlivePlayPacket(buf.readLong());
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerKeepAlivePlayPacket object) {
        buf.writeLong(object.keepAliveIdentifier());
    }
}