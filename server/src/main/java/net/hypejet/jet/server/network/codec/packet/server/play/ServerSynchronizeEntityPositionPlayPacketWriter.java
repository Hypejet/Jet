package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.vector.VectorNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizeEntityPositionPlayPacket;
import net.hypejet.jet.world.coordinate.Position;
import net.hypejet.jet.world.coordinate.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@linkplain NetworkWriter network writer}
 * of {@linkplain ServerSynchronizeEntityPositionPlayPacket server synchronize entity position play packets}.
 *
 * @since 1.0
 * @see ServerSynchronizeEntityPositionPlayPacket
 * @see NetworkWriter
 */
public final class ServerSynchronizeEntityPositionPlayPacketWriter
        implements NetworkWriter<ServerSynchronizeEntityPositionPlayPacket> {
    /**
     * An instance of the {@linkplain ServerSynchronizeEntityPositionPlayPacketWriter server
     * synchronize entity position play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerSynchronizeEntityPositionPlayPacketWriter
            INSTANCE = new ServerSynchronizeEntityPositionPlayPacketWriter();

    private ServerSynchronizeEntityPositionPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerSynchronizeEntityPositionPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.entityId());

        // Position in this packet is encoded differently
        Position position = object.position();
        VectorNetworkCodec.INSTANCE.write(buf, Vector.from(position));
        VectorNetworkCodec.INSTANCE.write(buf, object.velocity());
        buf.writeFloat(position.yaw());
        buf.writeFloat(position.pitch());

        buf.writeBoolean(object.onGround());
    }
}