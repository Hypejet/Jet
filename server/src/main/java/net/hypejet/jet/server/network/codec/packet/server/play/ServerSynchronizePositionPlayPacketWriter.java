package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.VectorNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizePositionPlayPacket.RelativeFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.EnumMap;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerSynchronizePositionPlayPacket a synchronize position play packet}.
 *
 * @since 1.0
 * @see ServerSynchronizePositionPlayPacket
 * @see NetworkWriter
 */
public final class ServerSynchronizePositionPlayPacketWriter
        implements NetworkWriter<ServerSynchronizePositionPlayPacket> {

    /**
     * An instance of the {@linkplain ServerSynchronizePositionPlayPacketWriter server synchronize position play packet
     * writer}.
     *
     * @since 1.0
     */
    public static final ServerSynchronizePositionPlayPacketWriter
            INSTANCE = new ServerSynchronizePositionPlayPacketWriter();

    private static final RelativeFlagCollectionNetworkWriter
            RELATIVE_FLAGS_WRITER = new RelativeFlagCollectionNetworkWriter();

    private ServerSynchronizePositionPlayPacketWriter() {}

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerSynchronizePositionPlayPacket object) {
        VarIntNetworkCodec.INSTANCE.write(buf, object.teleportId());
        VectorNetworkCodec.INSTANCE.write(buf, object.position());
        VectorNetworkCodec.INSTANCE.write(buf, object.deltaMovement());
        buf.writeFloat(object.yaw());
        buf.writeFloat(object.pitch());
        RELATIVE_FLAGS_WRITER.write(buf, object.relativeFlags());
    }

    /**
     * Represents {@linkplain NetworkWriter a network writer}, which reads and writes
     * {@linkplain Collection a collection} of {@linkplain RelativeFlag relative flags}.
     *
     * @since 1.0
     * @see RelativeFlag
     * @see Collection
     * @see NetworkWriter
     */
    private static final class RelativeFlagCollectionNetworkWriter implements NetworkWriter<Collection<RelativeFlag>> {

        private static final EnumMap<RelativeFlag, Integer> FLAG_IDS = new EnumMap<>(RelativeFlag.class);

        static {
            FLAG_IDS.put(RelativeFlag.RELATIVE_X, 0);
            FLAG_IDS.put(RelativeFlag.RELATIVE_Y, 1);
            FLAG_IDS.put(RelativeFlag.RELATIVE_Z, 2);
            FLAG_IDS.put(RelativeFlag.RELATIVE_YAW, 3);
            FLAG_IDS.put(RelativeFlag.RELATIVE_PITCH, 4);
            FLAG_IDS.put(RelativeFlag.RELATIVE_DELTA_X, 5);
            FLAG_IDS.put(RelativeFlag.RELATIVE_DELTA_Y, 6);
            FLAG_IDS.put(RelativeFlag.RELATIVE_DELTA_Z, 7);
            FLAG_IDS.put(RelativeFlag.ROTATE_DELTA, 8);
        }

        @Override
        public void write(@NonNull ByteBuf buf, @NonNull Collection<RelativeFlag> object) {
            int value = 0;
            for (RelativeFlag flag : object)
                value |= FLAG_IDS.get(flag);
            buf.writeInt(value);
        }
    }
}