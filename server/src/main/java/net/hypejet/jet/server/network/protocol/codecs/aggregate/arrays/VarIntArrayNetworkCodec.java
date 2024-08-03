package net.hypejet.jet.server.network.protocol.codecs.aggregate.arrays;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkCodec;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a {@linkplain AggregateNetworkCodec aggregate network codec}, which reads and writes an array
 * of variable-length integers.
 *
 * @since 1.0
 * @author Codestech
 * @see AggregateNetworkCodec
 */
public final class VarIntArrayNetworkCodec extends AggregateNetworkCodec<int[]> {

    private static final VarIntArrayNetworkCodec INSTANCE = new VarIntArrayNetworkCodec(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain VarIntArrayNetworkCodec variable-length integer array network codec}.
     *
     * @param maxLength a max length that an array can have
     * @since 1.0
     */
    private VarIntArrayNetworkCodec(int maxLength) {
        super(maxLength);
    }

    @Override
    protected int length(int @NonNull [] aggregate) {
        return aggregate.length;
    }

    @Override
    protected void encodeElements(int @NonNull [] aggregate, @NonNull ByteBuf buf) {
        for (int element : aggregate) {
            VarIntNetworkCodec.instance().write(buf, element);
        }
    }

    @Override
    protected int @NonNull [] decodeElements(int length, @NonNull ByteBuf buf) {
        int[] integers = new int[length];
        for (int index = 0; index < length; index++)
            integers[index] = VarIntNetworkCodec.instance().read(buf);
        return integers;
    }
    /**
     * Creates a new {@linkplain VarIntArrayNetworkCodec variable-length integer array network codec}.
     *
     * @return the instance or {@link #INSTANCE} if the max length is equal to {@link Integer#MAX_VALUE}
     * @since 1.0
     */
    public static @NonNull VarIntArrayNetworkCodec create(int maxLength) {
        if (maxLength == Integer.MAX_VALUE)
            return INSTANCE;
        return new VarIntArrayNetworkCodec(maxLength);
    }


    /**
     * Gets an instance of the {@linkplain VarIntArrayNetworkCodec variable-length array network codec}, which handles
     * arrays with size up to {@link Integer#MAX_VALUE}.
     *
     * @return the instance
     * @since 1.0
     */
    public static @NonNull VarIntArrayNetworkCodec instance() {
        return INSTANCE;
    }
}