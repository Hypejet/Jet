package net.hypejet.jet.server.network.codec.game.world.coordinate;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.codec.NetworkReader;
import net.hypejet.jet.world.coordinate.PositionFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Represents {@linkplain NetworkReader a network reader}, which reads {@linkplain Collection a collection}
 * of {@linkplain PositionFlag position flags}.
 *
 * @since 1.0
 * @see PositionFlag
 * @see Collection
 * @see NetworkReader
 */
public final class PositionFlagsReader implements NetworkReader<Collection<PositionFlag>> {

    private static final IdentityHashMap<PositionFlag, Integer> FLAG_IDS = new IdentityHashMap<>();

    /**
     * An instance of the {@linkplain PositionFlagsReader position flags reader}.
     *
     * @since 1.0
     */
    public static final PositionFlagsReader INSTANCE = new PositionFlagsReader();

    static {
        FLAG_IDS.put(PositionFlag.ON_GROUND, 1);
        FLAG_IDS.put(PositionFlag.HORIZONTAL_COLLISION, 2);
    }

    private PositionFlagsReader() {}

    @Override
    public @NonNull Collection<PositionFlag> read(@NonNull ByteBuf buf) {
        short value = buf.readUnsignedByte();
        Set<PositionFlag> flags = new HashSet<>();

        for (PositionFlag flag : PositionFlag.VALUES) {
            int id = FLAG_IDS.get(flag);
            if ((value & id) != 0)
                flags.add(flag);
        }

        return Set.copyOf(flags);
    }
}