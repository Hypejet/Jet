package net.hypejet.jet.server.network.protocol.codecs.position;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.protocol.position.PositionFlag;
import net.hypejet.jet.server.network.codec.NetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a {@linkplain NetworkCodec network codec}, which reads and writes a {@linkplain Collection collection}
 * of {@linkplain PositionFlag position flags}.
 *
 * @since 1.0
 * @author Codestech
 * @see PositionFlag
 * @see Collection
 * @see NetworkCodec
 */
public final class PositionFlagsCodec implements NetworkCodec<Collection<PositionFlag>> {

    private static final PositionFlagsCodec INSTANCE = new PositionFlagsCodec();
    private static final EnumMap<PositionFlag, Integer> FLAG_IDS = new EnumMap<>(PositionFlag.class);

    static {
        FLAG_IDS.put(PositionFlag.ON_GROUND, 1);
        FLAG_IDS.put(PositionFlag.HORIZONTAL_COLLISION, 2);
    }

    private PositionFlagsCodec() {}

    @Override
    public @NonNull Collection<PositionFlag> read(@NonNull ByteBuf buf) {
        short value = buf.readUnsignedByte();
        Set<PositionFlag> flags = new HashSet<>();

        for (PositionFlag flag : PositionFlag.values()) {
            int id = FLAG_IDS.get(flag);
            if ((value & id) != 0)
                flags.add(flag);
        }

        return Set.copyOf(flags);
    }

    @Override
    public void write(@NonNull ByteBuf buf, @NonNull Collection<PositionFlag> object) {
        short value = 0;
        for (PositionFlag flag : object)
            value |= FLAG_IDS.get(flag);
        buf.writeByte(value);
    }

    /**
     * Gets an instance of the {@linkplain PositionFlagsCodec position flags codec}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull PositionFlagsCodec instance() {
        return INSTANCE;
    }
}