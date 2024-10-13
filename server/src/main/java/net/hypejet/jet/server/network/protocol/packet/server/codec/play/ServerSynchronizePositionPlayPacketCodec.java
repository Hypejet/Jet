package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket.RelativeFlag;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.coordinate.VectorNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.packet.PacketCodec;
import net.hypejet.jet.server.network.protocol.packet.server.ServerPacketIdentifiers;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a {@linkplain PacketCodec packet codec}, which reads and writes
 * a {@linkplain ServerSynchronizePositionPlayPacket synchronize position play packet}.
 *
 * @since 1.0
 * @author Codestech
 * @see ServerSynchronizePositionPlayPacket
 * @see PacketCodec
 */
public final class ServerSynchronizePositionPlayPacketCodec extends PacketCodec<ServerSynchronizePositionPlayPacket> {

    private static final RelativeFlagCollectionNetworkCodec FLAG_CODEC = new RelativeFlagCollectionNetworkCodec();

    /**
     * Constructs the {@linkplain ServerSynchronizePositionPlayPacket synchronize position play packet}.
     *
     * @since 1.0
     */
    public ServerSynchronizePositionPlayPacketCodec() {
        super(ServerPacketIdentifiers.PLAY_SYNCHRONIZE_POSITION, ServerSynchronizePositionPlayPacket.class);
    }

    @Override
    public @NonNull ServerSynchronizePositionPlayPacket read(@NonNull ByteBuf buf) {
        return new ServerSynchronizePositionPlayPacket(VarIntNetworkCodec.instance().read(buf),
                VectorNetworkCodec.instance().read(buf), VectorNetworkCodec.instance().read(buf),
                buf.readFloat(), buf.readFloat(), FLAG_CODEC.read(buf)
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerSynchronizePositionPlayPacket object) {
        VarIntNetworkCodec.instance().write(buf, object.teleportId());
        VectorNetworkCodec.instance().write(buf, object.position());
        VectorNetworkCodec.instance().write(buf, object.deltaMovement());
        buf.writeFloat(object.yaw());
        buf.writeFloat(object.pitch());
        FLAG_CODEC.write(buf, object.relativeFlags());
    }

    /**
     * Represents a {@linkplain NetworkCodec network codec}, which reads and writes
     * a {@linkplain Collection collection} of {@linkplain RelativeFlag relative flags}.
     *
     * @since 1.0
     * @author Codestech
     * @see RelativeFlag
     * @see Collection
     * @see NetworkCodec
     */
    private static final class RelativeFlagCollectionNetworkCodec implements NetworkCodec<Collection<RelativeFlag>> {

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
        public @NonNull Collection<RelativeFlag> read(@NonNull ByteBuf buf) {
            int value = buf.readInt();
            Set<RelativeFlag> flags = new HashSet<>();

            for (RelativeFlag flag : RelativeFlag.values()) {
                if ((value & FLAG_IDS.get(flag)) != 0) {
                    flags.add(flag);
                }
            }

            return flags;
        }

        @Override
        public void write(@NonNull ByteBuf buf, @NonNull Collection<RelativeFlag> object) {
            byte value = 0;
            for (RelativeFlag flag : object)
                value |= FLAG_IDS.get(flag);
            buf.writeInt(value);
        }
    }
}