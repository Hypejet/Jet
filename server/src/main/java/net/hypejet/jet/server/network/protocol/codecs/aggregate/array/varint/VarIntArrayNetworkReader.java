package net.hypejet.jet.server.network.protocol.codecs.aggregate.array.varint;

import io.netty.buffer.ByteBuf;
import net.hypejet.jet.server.network.protocol.codecs.aggregate.AggregateNetworkReader;
import net.hypejet.jet.server.network.protocol.codecs.number.VarIntNetworkCodec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents {@linkplain AggregateNetworkReader an aggregate network reader}, which reads a variable-length integer
 * array.
 *
 * @since 1.0
 * @author Codestech
 * @see AggregateNetworkReader
 */
public final class VarIntArrayNetworkReader extends AggregateNetworkReader<int[]> {

    /**
     * An instance of {@linkplain VarIntArrayNetworkReader a variable-length integer array network reader}, which
     * supports lengths up to {@link Integer#MAX_VALUE}.
     *
     * @since 1.0
     */
    public static final VarIntArrayNetworkReader INSTANCE = new VarIntArrayNetworkReader(Integer.MAX_VALUE);

    /**
     * Constructs the {@linkplain VarIntArrayNetworkReader variable-length integer array}.
     *
     * @param maxLength a max length that an array can have
     * @since 1.0
     */
    public VarIntArrayNetworkReader(int maxLength) {
        super(maxLength);
    }

    @Override
    protected int @NonNull [] decodeElements(int length, @NonNull ByteBuf buf) {
        int[] integers = new int[length];
        for (int index = 0; index < length; index++)
            integers[index] = VarIntNetworkCodec.INSTANCE.read(buf);
        return integers;
    }
}