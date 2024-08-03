package net.hypejet.jet.server.network.protocol.packet.server.codec.play;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket;
import net.hypejet.jet.protocol.packet.server.play.ServerSynchronizePositionPlayPacket.RelativeFlag;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.other.PositionNetworkCodec;
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
        return new ServerSynchronizePositionPlayPacket(
                PositionNetworkCodec.instance().read(buf),
                FLAG_CODEC.read(buf),
                VarIntNetworkCodec.instance().read(buf)
        );
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull ServerSynchronizePositionPlayPacket object) {
        PositionNetworkCodec.instance().write(buf, object.position());
        FLAG_CODEC.write(buf, object.relativeFlags());
        VarIntNetworkCodec.instance().write(buf, object.teleportId());
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
            FLAG_IDS.put(RelativeFlag.RELATIVE_X, 0x01);
            FLAG_IDS.put(RelativeFlag.RELATIVE_Y, 0x02);
            FLAG_IDS.put(RelativeFlag.RELATIVE_Z, 0x04);
            FLAG_IDS.put(RelativeFlag.RELATIVE_YAW, 0x08);
            FLAG_IDS.put(RelativeFlag.RELATIVE_PITCH, 0x10);
        }

        @Override
        public @NonNull Collection<RelativeFlag> read(@NonNull ByteBuf buf) {
            byte value = buf.readByte();
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
            buf.writeByte(value);
        }
    }
}