package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.vector.ShortVectorNetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerEntityVelocityPlayPacket;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerEntityVelocityPlayPacket server entity velocity play packets}.
 *
 * @since 1.0
 * @see ServerEntityVelocityPlayPacket
 * @see NetworkWriter
 */
public final class ServerEntityVelocityPlayPacketWriter implements NetworkWriter<ServerEntityVelocityPlayPacket> {
    /**
     * An instance of the {@linkplain ServerEntityVelocityPlayPacketWriter server entity velocity play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerEntityVelocityPlayPacketWriter INSTANCE = new ServerEntityVelocityPlayPacketWriter();

    private ServerEntityVelocityPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerEntityVelocityPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.entityId());
        ShortVectorNetworkWriter.INSTANCE.write(buf, object.velocity());
    }
}