package net.hypejet.jet.server.network.codec.packet.server.play;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityAnimationPlayPacket;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerEntityAnimationPlayPacket a server entity animation play packet}.
 *
 * @since 1.0
 * @see ServerEntityAnimationPlayPacket
 * @see NetworkWriter
 */
public final class ServerEntityAnimationPlayPacketWriter implements NetworkWriter<ServerEntityAnimationPlayPacket> {
    
    /**
     * An instance of the {@linkplain ServerEntityAnimationPlayPacketWriter server entity animation play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityAnimationPlayPacketWriter INSTANCE = new ServerEntityAnimationPlayPacketWriter();

    private ServerEntityAnimationPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityAnimationPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.entityId());
        VarIntNetworkCodec.INSTANCE.write(buf, object.animation());
    }
}
