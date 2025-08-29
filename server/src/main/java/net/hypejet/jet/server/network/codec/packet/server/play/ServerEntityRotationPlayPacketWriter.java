package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.AngleNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityRotationPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntityRotationPlayPacket server entity rotation play packets}.
 *
 * @since 1.0
 * @see ServerEntityRotationPlayPacket
 * @see NetworkWriter
 */
public final class ServerEntityRotationPlayPacketWriter implements NetworkWriter<ServerEntityRotationPlayPacket> {
    /**
     * An instance of the {@linkplain ServerEntityRotationPlayPacketWriter server entity rotation play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityRotationPlayPacketWriter INSTANCE = new ServerEntityRotationPlayPacketWriter();

    private ServerEntityRotationPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityRotationPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.entityId());
        AngleNetworkWriter.INSTANCE.write(buf, object.yaw());
        AngleNetworkWriter.INSTANCE.write(buf, object.pitch());
        buf.writeBoolean(object.onGround());
    }
}