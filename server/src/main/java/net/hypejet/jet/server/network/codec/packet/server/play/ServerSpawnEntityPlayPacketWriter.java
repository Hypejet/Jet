
package net.hypejet.jet.server.network.codec.packet.server.play;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.PositionNetworkCodec;
import net.hypejet.jet.server.network.codec.game.world.coordinate.VectorNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.codec.other.UUIDNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSpawnEntityPlayPacket;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerSpawnEntityPlayPacket a spawn entity play packet}.
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
        PositionNetworkCodec.INSTANCE.write(buf, object.position());
        buf.writeByte((byte) (object.headYaw() * 256.0f / 360.0f));
        VarIntNetworkCodec.INSTANCE.write(buf, object.data());
        VectorNetworkCodec.INSTANCE.write(buf, object.velocity());
    }
}

