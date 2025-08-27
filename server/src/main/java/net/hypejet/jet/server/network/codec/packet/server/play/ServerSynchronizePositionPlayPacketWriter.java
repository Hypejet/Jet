package net.hypejet.jet.server.network.codec.packet.server.play;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.world.coordinate.flag.RelativeFlag;
import net.hypejet.jet.server.network.codec.NetworkWriter;
import net.hypejet.jet.server.network.codec.game.world.coordinate.vector.VectorNetworkCodec;
import net.hypejet.jet.server.network.codec.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.packet.packets.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.world.coordinate.Position;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents {@linkplain NetworkWriter a network writer}, which writes
 * {@linkplain ServerSynchronizePositionPlayPacket a server synchronize position play packet}.
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
        VarIntNetworkCodec.INSTANCE.write(buf, object.identifier());

        Position position = object.position();
        buf.writeDouble(position.x());
        buf.writeDouble(position.y());
        buf.writeDouble(position.z());

        VectorNetworkCodec.INSTANCE.write(buf, object.deltaMovement());

        buf.writeFloat(position.yaw());
        buf.writeFloat(position.pitch());

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

        private static final Object2IntMap<RelativeFlag> FLAG_MASKS = new Object2IntOpenHashMap<>();

        static {
            FLAG_MASKS.put(RelativeFlag.X, 0x01);
            FLAG_MASKS.put(RelativeFlag.Y, 0x02);
            FLAG_MASKS.put(RelativeFlag.Z, 0x04);
            FLAG_MASKS.put(RelativeFlag.YAW, 0x08);
            FLAG_MASKS.put(RelativeFlag.PITCH, 0x10);
            FLAG_MASKS.put(RelativeFlag.VELOCITY_X, 0x20);
            FLAG_MASKS.put(RelativeFlag.VELOCITY_Y, 0x40);
            FLAG_MASKS.put(RelativeFlag.VELOCITY_Z, 0x80);
            FLAG_MASKS.put(RelativeFlag.ROTATE_VELOCITY, 0x100);
        }

        @Override
        public void write(@NonNull ByteBuf buf, @NonNull Collection<RelativeFlag> object) {
            int value = 0;
            for (RelativeFlag flag : object) {
                if (!FLAG_MASKS.containsKey(flag)) {
                    throw new IllegalArgumentException(String.format(
                            "Could not find an identifier of a relative flag of %s", flag
                    ));
                }
                value |= FLAG_MASKS.getInt(flag);
            }
            buf.writeInt(value);
        }
    }
}