package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerCenterChunkPlayPacket;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads amd writes
 * a {@linkplain ServerCenterChunkPlayPacket center chunk play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerCenterChunkPlayPacket
 * @see PacketCodec
 */
public final class ServerCenterChunkPlayPacketCodec extends PacketCodec<ServerCenterChunkPlayPacket> {
    /**
     * Constructs the {@linkplain ServerCenterChunkPlayPacketCodec center chunk play packet codec}.
     *
     * @since 1.0
     */
    public ServerCenterChunkPlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_CENTER_CHUNK, ServerCenterChunkPlayPacket.class);
    }

    @Override
    public @NonNull ServerCenterChunkPlayPacket read(@NonNull ByteBuf buf) {
        return new ServerCenterChunkPlayPacket(VarIntNetworkCodec.instance().read(buf),
                VarIntNetworkCodec.instance().read(buf));
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerCenterChunkPlayPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.chunkX());
        VarIntNetworkCodec.instance().write(buf, object.chunkZ());
    }
}