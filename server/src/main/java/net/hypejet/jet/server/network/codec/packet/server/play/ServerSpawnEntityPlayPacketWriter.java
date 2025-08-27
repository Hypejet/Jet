
package net.hypejet.jet.server.network.codec.packet.server.play;

import net.hypejet.jet.server.network.codec.game.world.coordinate.AngleNetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.vector.ShortVectorNetworkWriter;
import net.hypejet.jet.world.coordinate.Position;
import org.checkerframework.checker.nullness.qual.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSpawnEntityPlayPacket;

/**
 * A {@linkplain NetworkWriter network writer} of {@linkplain ServerSpawnEntityPlayPacket spawn entity play packets}.
 *
 * @since 1.0
 * @see ServerSpawnEntityPlayPacket
 * @see NetworkWriter
 */
public final class ServerSpawnEntityPlayPacketWriter implements NetworkWriter<ServerSpawnEntityPlayPacket> {
    /**
     * An instance of the {@linkplain ServerSpawnEntityPlayPacketWriter server spawn entity play packet writer}.
     *
     * @since 1.0
     */
    public static final ServerSpawnEntityPlayPacketWriter INSTANCE = new ServerSpawnEntityPlayPacketWriter();

    private ServerSpawnEntityPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerSpawnEntityPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.entityId());
        UUIDNetworkCodec.INSTANCE.write(buf, object.uniqueId());
        VarIntNetworkCodec.INSTANCE.write(buf, object.type());

        // Positions in this packet are written differently
        Position position = object.position();
        buf.writeDouble(position.x());
        buf.writeDouble(position.y());
        buf.writeDouble(position.z());
        AngleNetworkWriter.INSTANCE.write(buf, position.pitch());
        AngleNetworkWriter.INSTANCE.write(buf, position.yaw());
        AngleNetworkWriter.INSTANCE.write(buf, object.headYaw());

        VarIntNetworkCodec.INSTANCE.write(buf, object.data());
        ShortVectorNetworkWriter.INSTANCE.write(buf, object.velocity());
    }
}